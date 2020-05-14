package com.mefollow.webschool.checker.application;

import com.mefollow.webschool.checker.domain.base.*;
import com.mefollow.webschool.checker.domain.dto.CheckerErrorResultDto;
import com.mefollow.webschool.checker.domain.dto.CheckerResultDto;
import com.mefollow.webschool.checker.infrastructure.repository.CheckRuleRepository;
import com.mefollow.webschool.sandbox.infrastructure.repository.ChapterRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.io.IOException;
import java.util.*;

import static com.mefollow.webschool.checker.domain.base.CheckerException.HTML_CHECK_RULES_CANNOT_BE_EMPTY;
import static com.mefollow.webschool.sandbox.domain.base.SandboxException.CHAPTER_NOT_FOUND;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class RuleCheckerService {

    private final CssParserService cssParserService;
    private final ChapterRepository chapterRepository;
    private final CheckRuleRepository checkRuleRepository;

    public RuleCheckerService(CssParserService cssParserService,
                              ChapterRepository chapterRepository,
                              CheckRuleRepository checkRuleRepository) {
        this.cssParserService = cssParserService;
        this.chapterRepository = chapterRepository;
        this.checkRuleRepository = checkRuleRepository;
    }

    public Mono<CheckerResultDto> checkRules(String chapterId, String htmlContent, @Nullable String cssContent, @Nullable String jsContent) {
        return chapterRepository.findById(chapterId)
                .switchIfEmpty(error(CHAPTER_NOT_FOUND))
                .flatMap(chapter -> checkRuleRepository.findAllByChapterIdAndType(chapter.getId(), CheckRuleType.CSS)
                        .cast(CssCheckRule.class)
                        .collectList()
                        .flatMap(cssCheckRules -> checkRuleRepository.findAllByChapterIdAndType(chapter.getId(), CheckRuleType.JS)
                                .cast(JsCheckRule.class)
                                .collectList()
                                .flatMap(jsCheckRules -> checkRuleRepository.findAllByChapterIdAndType(chapter.getId(), CheckRuleType.HTML)
                                        .cast(HtmlCheckRule.class)
                                        .collectList()
                                        .flatMap(htmlCheckRules -> {
                                            if (htmlCheckRules.isEmpty()) return error(HTML_CHECK_RULES_CANNOT_BE_EMPTY);

                                            final var htmlDom = Jsoup.parse(htmlContent);
                                            final var cssStyles = cssParserService.parseStyles(htmlDom, cssContent);
                                            CheckerErrorResultDto firstCssError = null;
                                            for (HtmlCheckRule htmlCheckRule : htmlCheckRules) {
                                                final var checkerResult = processHtmlAndCssCheckRules(htmlDom, htmlCheckRule, cssCheckRules, cssStyles);

                                                if (checkerResult instanceof CheckerErrorResultDto) {
                                                    if (CheckRuleType.HTML == ((CheckerErrorResultDto) checkerResult).getErrorSource()) return just(checkerResult);
                                                    if (CheckRuleType.CSS == ((CheckerErrorResultDto) checkerResult).getErrorSource())
                                                        firstCssError = firstCssError == null ? (CheckerErrorResultDto) checkerResult : firstCssError;
                                                }
                                            }

                                            if (firstCssError != null) return just(firstCssError);
                                            if (jsCheckRules.size() == 1 && jsContent != null) {
                                                final var checkerResult = processJsCheckRules(jsCheckRules.get(0), jsContent);
                                                if (checkerResult instanceof CheckerErrorResultDto) {
                                                    return just(checkerResult);
                                                }
                                            }

                                            return just(new CheckerResultDto("Все верно, можешь чилить и переходить к новому заданию!"));
                                        }))));
    }

    private CheckerResultDto processHtmlAndCssCheckRules(Element root, HtmlCheckRule htmlCheckRule, List<CssCheckRule> cssCheckRules, Map<String, Map<String, Tuple2<String, Priority>>> cssStyles) {
        final var nodes = root.select(htmlCheckRule.getCssSelector());
        if (nodes.size() != 1) return new CheckerErrorResultDto("Что-то не так со структурой файла index.html.", CheckRuleType.HTML);

        final var node = nodes.get(0);
        if (node == null) return new CheckerErrorResultDto(htmlCheckRule.getCommonErrorMessage(), CheckRuleType.HTML);
        if (!htmlCheckRule.getTagName().getValue().equals(node.tag().getName())) return new CheckerErrorResultDto(htmlCheckRule.getCommonErrorMessage(), CheckRuleType.HTML);

        if (htmlCheckRule.hasTagId() && !htmlCheckRule.getTagId().equals(node.id())) return new CheckerErrorResultDto(htmlCheckRule.getTagIdErrorMessage(), CheckRuleType.HTML);
        if (htmlCheckRule.hasTagClasses()) {
            final var checkResult = checkClasses(htmlCheckRule, node.classNames());
            if (checkResult instanceof CheckerErrorResultDto) return checkResult;
        }
        if (htmlCheckRule.hasTagAttributes()) {
            final var checkResult = checkAttributes(htmlCheckRule, node.attributes());
            if (checkResult instanceof CheckerErrorResultDto) return checkResult;
        }
        if (htmlCheckRule.hasTagInnerContent() && !htmlCheckRule.getTagInnerContent().equals(node.html())) return new CheckerErrorResultDto(htmlCheckRule.getTagInnerContentErrorMessage(), CheckRuleType.HTML);

        final var cssCheckRule = cssCheckRules.stream().filter(item -> item.getHtmlCheckRuleId().equals(htmlCheckRule.getId())).findFirst();
        if (cssCheckRule.isPresent()) {
            final var appliedStyleProperties = new HashMap<String, Tuple2<String, Priority>>();
            final var inlineStyleAttrValue = node.attr("style");
            if (isNotBlank(inlineStyleAttrValue)) {
                try {
                    appliedStyleProperties.putAll(cssParserService.parseCssDeclarationAsInline(inlineStyleAttrValue));
                } catch (IOException e) {
                    return new CheckerErrorResultDto(format("Что-то не так с внутренними стилями для элемента %s.", node.tag().getName()), CheckRuleType.CSS);
                }
            }

            for (Map.Entry<String, Map<String, Tuple2<String, Priority>>> cssStyle : cssStyles.entrySet()) {
                final var cssSelector = cssStyle.getKey();
                if (node.is(cssSelector)) {
                    cssStyle.getValue().forEach((property, propertyValue) -> {
                        appliedStyleProperties.compute(property, (propertyKey, currentPropertyValue) -> {
                            if (currentPropertyValue == null) return propertyValue;
                            return currentPropertyValue.getT2().isLowerThan(propertyValue.getT2())
                                    ? propertyValue
                                    : currentPropertyValue;
                        });
                    });
                }
            }

            final var properties = appliedStyleProperties.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getT1()));
            for (Map.Entry<String, CssCheckRule.CssProperty> property : cssCheckRule.get().getCssProperties().entrySet()) {
                final var propertyName = property.getKey();
                final var propertyValue = property.getValue().getPropertyValue();
                final var propertyErrorMessage = property.getValue().getErrorMessage();

                final var appliedProperty = properties.get(propertyName);
                if (appliedProperty == null || !appliedProperty.equals(propertyValue)) return new CheckerErrorResultDto(propertyErrorMessage, CheckRuleType.CSS);
            }
        }

        return new CheckerResultDto();
    }

    private CheckerResultDto checkAttributes(HtmlCheckRule htmlCheckRule, Attributes attributes) {
        final var receivedAttributes = attributes.asList().stream().collect(toMap(Attribute::getKey, Attribute::getValue));
        for (Map.Entry<String, HtmlCheckRule.HtmlAttribute> htmlAttribute : htmlCheckRule.getTagAttributes().entrySet()) {
            final var receivedAttributeValue = receivedAttributes.get(htmlAttribute.getKey());
            if (receivedAttributeValue == null || !Objects.equals(receivedAttributeValue, htmlAttribute.getValue().getAttributeValue()))
                return new CheckerErrorResultDto(htmlAttribute.getValue().getErrorMessage(), CheckRuleType.HTML);
        }

        return new CheckerResultDto();
    }

    private CheckerResultDto checkClasses(HtmlCheckRule htmlCheckRule, Set<String> classNames) {
        for (HtmlCheckRule.HtmlClass htmlClass : htmlCheckRule.getTagClasses()) {
            final var classNameNotExist = classNames.stream().filter(className -> Objects.equals(className, htmlClass.getClassName())).count() != 1;
            if (classNameNotExist) return new CheckerErrorResultDto(htmlClass.getErrorMessage(), CheckRuleType.HTML);
        }

        return new CheckerResultDto();
    }

    private CheckerResultDto processJsCheckRules(JsCheckRule checkRule, String jsContent) {
        final var patternForCommentsAndSpaceSymbols = "(//.*)|(\\s)";
        final var replacementForCommentsAndSpaceSymbols = "";
        final var patternForQuotes = "\'";
        final var replacementForQuotes = "\"";

        final var clearedJsContent = jsContent
                .replaceAll(patternForCommentsAndSpaceSymbols, replacementForCommentsAndSpaceSymbols)
                .replaceAll(patternForQuotes, replacementForQuotes);
        final var clearedCheckRule = checkRule.getContent()
                .replaceAll(patternForCommentsAndSpaceSymbols,replacementForCommentsAndSpaceSymbols)
                .replaceAll(patternForQuotes, replacementForQuotes);

        return clearedCheckRule.equals(clearedJsContent)
                ? new CheckerResultDto()
                : new CheckerErrorResultDto(checkRule.getCommonErrorMessage(), CheckRuleType.JS);
    }
}
