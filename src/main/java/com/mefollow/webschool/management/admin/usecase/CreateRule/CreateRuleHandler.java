package com.mefollow.webschool.management.admin.usecase.CreateRule;

import com.mefollow.webschool.checker.domain.base.*;
import com.mefollow.webschool.checker.domain.base.CssCheckRule.CssProperty;
import com.mefollow.webschool.checker.domain.base.HtmlCheckRule.HtmlAttribute;
import com.mefollow.webschool.checker.domain.base.HtmlCheckRule.HtmlClass;
import com.mefollow.webschool.checker.infrastructure.repository.CheckRuleRepository;
import com.mefollow.webschool.management.admin.application.PermissionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.mefollow.webschool.checker.domain.base.CheckRuleType.*;
import static com.mefollow.webschool.management.admin.domain.base.ManagementException.*;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Service
public class CreateRuleHandler {

    private final PermissionService permissionService;
    private final CheckRuleRepository checkRuleRepository;

    public CreateRuleHandler(PermissionService permissionService,
                             CheckRuleRepository checkRuleRepository) {
        this.permissionService = permissionService;
        this.checkRuleRepository = checkRuleRepository;
    }

    public Mono<CheckRule> handle(CreateRuleCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<CheckRule> processHandling(CreateRuleCommand command) {
        return checkRuleRepository.findAllByChapterId(command.getChapterId())
                .collectList()
                .flatMap(rules -> {
                    switch (command.getType()) {
                        case HTML:
                            return createHtmlCheckRule(command, rules);
                        case CSS:
                            return createCssCheckRule(command, rules);
                        case JS:
                            return createJsCheckRule(command, rules);
                        default:
                            return error(WRONG_RULE_TYPE);
                    }
                })
                .flatMap(checkRuleRepository::save);
    }

    private Mono<CheckRule> createHtmlCheckRule(CreateRuleCommand command, List<CheckRule> rules) {
        if (isBlank(command.getCssSelector())) return error(PAYLOAD_DATA_INCONSISTENCY_HTML_RULE_CSS_SELECTOR);
        if (isBlank(command.getTagName()) || HtmlTag.getByValue(command.getTagName()) == null) return error(PAYLOAD_DATA_INCONSISTENCY_HTML_RULE_TAG_NAME);
        final var duplicate = rules.stream()
                .filter(rule -> HTML == rule.getType())
                .map(rule -> (HtmlCheckRule) rule)
                .filter(rule -> rule.getCssSelector().equalsIgnoreCase(command.getCssSelector()))
                .findAny();
        if (duplicate.isPresent()) return error(HTML_RULE_WITH_SUCH_CSS_SELECTOR_ALREADY_EXISTS);

        final var htmlCheckRule = new HtmlCheckRule(command.getChapterId(), command.getCommonErrorMessage(), command.getCssSelector(), HtmlTag.getByValue(command.getTagName()));
        if (isNotBlank(command.getTagId()) && isNotBlank(command.getTagIdErrorMessage())) {
            htmlCheckRule.setTagId(command.getTagId());
            htmlCheckRule.setTagIdErrorMessage(command.getTagIdErrorMessage());
        }

        if (isNotEmpty(command.getTagClasses())) {
            final var tagClasses = new HashSet<HtmlClass>();
            command.getTagClasses().forEach(tagClass -> {
                if (isNotBlank(tagClass.getClassName()) && isNotBlank(tagClass.getErrorMessage())) {
                    tagClasses.add(new HtmlClass(tagClass.getClassName(), tagClass.getErrorMessage()));
                }
            });
            htmlCheckRule.setTagClasses(tagClasses);
        }

        if (!command.getTagAttributes().isEmpty()) {
            final var tagAttributes = new HashMap<String, HtmlAttribute>();
            command.getTagAttributes().forEach((attributeName, attribute) -> {
                if (attribute != null && isNotBlank(attribute.getAttributeValue()) && isNotBlank(attribute.getErrorMessage())) {
                    tagAttributes.put(attributeName, new HtmlAttribute(attribute.getAttributeValue(), attribute.getErrorMessage()));
                }
            });
            htmlCheckRule.setTagAttributes(tagAttributes);
        }

        if (isNotBlank(command.getTagInnerContent()) && isNotBlank(command.getTagInnerContentErrorMessage())) {
            htmlCheckRule.setTagInnerContent(command.getTagInnerContent());
            htmlCheckRule.setTagInnerContentErrorMessage(command.getTagInnerContentErrorMessage());
        }

        return just(htmlCheckRule);
    }

    private Mono<CheckRule> createCssCheckRule(CreateRuleCommand command, List<CheckRule> rules) {
        if (isBlank(command.getHtmlCheckRuleId())) return error(PAYLOAD_DATA_INCONSISTENCY_CSS_RULE_TAG_REFERENCE);
        final var duplicate = rules.stream()
                .filter(rule -> CSS == rule.getType())
                .map(rule -> (CssCheckRule) rule)
                .filter(rule -> rule.getHtmlCheckRuleId().equalsIgnoreCase(command.getHtmlCheckRuleId()))
                .findAny();
        if (duplicate.isPresent()) return error(CSS_RULE_WITH_SUCH_HTML_RULE_REFERENCE_ALREADY_EXISTS);

        final var cssCheckRule = new CssCheckRule(command.getChapterId(), command.getCommonErrorMessage(), command.getHtmlCheckRuleId());
        if (!command.getCssProperties().isEmpty()) {
            final var cssProperties = new HashMap<String, CssProperty>();
            command.getCssProperties().forEach((propertyName, property) -> {
                if (property != null && isNotBlank(property.getPropertyValue()) && isNotBlank(property.getErrorMessage())) {
                    cssProperties.put(propertyName, new CssProperty(property.getPropertyValue(), property.getErrorMessage()));
                }
            });
            cssCheckRule.setCssProperties(cssProperties);
        }

        return just(cssCheckRule);
    }

    private Mono<CheckRule> createJsCheckRule(CreateRuleCommand command, List<CheckRule> rules) {
        if (command.getExecutable() == null) return error(PAYLOAD_DATA_INCONSISTENCY_JS_RULE_EXECUTABLE);
        if (isBlank(command.getContent())) return error(PAYLOAD_DATA_INCONSISTENCY_JS_RULE_CONTENT);
        final var duplicate = rules.stream()
                .filter(rule -> JS == rule.getType())
                .findAny();
        if (duplicate.isPresent()) return error(JS_RULE_ALREADY_EXISTS);

        return just(new JsCheckRule(command.getChapterId(), command.getCommonErrorMessage(), command.getContent(), command.getExecutable()));
    }
}
