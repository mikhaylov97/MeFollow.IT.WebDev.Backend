package com.mefollow.webschool.checker.application;

import com.mefollow.webschool.checker.domain.base.Priority;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import org.jsoup.nodes.Document;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleRule;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class CssParserService {

    public Map<String, Map<String, Tuple2<String, Priority>>> parseStyles(Document htmlDom, @Nullable String cssContentFromFile) {
        final var cssStyles = new HashMap<String, Map<String, Tuple2<String, Priority>>>();
        final var styles = htmlDom.select("style, link[rel*=stylesheet][href*=style.css]");
        for (int i = 0; i < styles.size(); i++) {
            try {
                final Map<String, Map<String, Tuple2<String, Priority>>> parsedStyles;
                if (styles.get(i).is("link[rel*=stylesheet][href*=style.css]") && cssContentFromFile != null) {
                    parsedStyles = parseCssRuleList(cssContentFromFile, i);
                } else {
                    parsedStyles = parseCssRuleList(styles.get(i).html(), i);
                }

                parsedStyles.forEach((selector, parsedProperties) -> {
                    if (!selector.contains(":")) {
                        cssStyles.compute(selector, (selectorKey, currentProperties) -> {
                            if (currentProperties == null) return new HashMap<>(parsedProperties);
                            parsedProperties.forEach((parsedProperty, parsedPropertyTuple) -> {
                                final var parsedPropertyValue = parsedPropertyTuple.getT1();
                                final var parsedPropertyValuePriority = parsedPropertyTuple.getT2();
                                currentProperties.compute(parsedProperty, (currentPropertyKey, currentPropertyTuple) -> {
                                    if (currentPropertyTuple == null)
                                        return Tuples.of(parsedPropertyValue, parsedPropertyValuePriority);
                                    return currentPropertyTuple.getT2().isLowerThan(parsedPropertyValuePriority)
                                            ? Tuples.of(parsedPropertyValue, parsedPropertyValuePriority)
                                            : currentPropertyTuple;
                                });
                            });

                            return currentProperties;
                        });
                    }
                });
            } catch (IOException e) {
                //
            }
        }

        return cssStyles;
    }

    private Map<String, Tuple2<String, Priority>> parseCssDeclarationAsPriority(String cssContent, int priority) throws IOException {
        return parseCssDeclaration(cssContent, priority);
    }

    public Map<String, Tuple2<String, Priority>> parseCssDeclarationAsInline(String cssContent) throws IOException {
        return parseCssDeclaration(cssContent, null);
    }

    private Map<String, Tuple2<String, Priority>> parseCssDeclaration(String cssContent, @Nullable Integer priority) throws IOException {
        final var style = new HashMap<String, Tuple2<String, Priority>>();

        final var source = new InputSource(new StringReader(cssContent));
        final var parser = new CSSOMParser(new SACParserCSS3());
        final var styleDeclaration = parser.parseStyleDeclaration(source);
        for (int i = 0; i < styleDeclaration.getLength(); i++) {
            final var property = styleDeclaration.item(i);
            final var propertyValue = styleDeclaration.getPropertyValue(property);
            final var isImportant = "important".equals(styleDeclaration.getPropertyPriority(property));
            if (priority != null) {
                style.put(property, Tuples.of(propertyValue, new Priority(priority, isImportant)));
            } else {
                style.put(property, Tuples.of(propertyValue, new Priority(true, isImportant)));
            }
        }

        return style;
    }

    private Map<String, Map<String, Tuple2<String, Priority>>> parseCssRuleList(String cssContent, int priority) throws IOException {
        final var parsedStyles = new HashMap<String, Map<String, Tuple2<String, Priority>>>();

        final var source = new InputSource(new StringReader(cssContent));
        final var parser = new CSSOMParser(new SACParserCSS3());
        final var styleSheet = parser.parseStyleSheet(source, null, null);
        final var ruleList = styleSheet.getCssRules();
        for (int i = 0; i < ruleList.getLength(); i++) {
            final var rule = ruleList.item(i);
            if (rule instanceof CSSStyleRule) {
                final var castedRule = (CSSStyleRule) rule;

                final var properties = new HashMap<String, Tuple2<String, Priority>>();
                parsedStyles.put(castedRule.getSelectorText(), properties);

                final var styleDeclaration = castedRule.getStyle();
                for (int j = 0; j < styleDeclaration.getLength(); j++) {
                    final var property = styleDeclaration.item(j);
                    final var propertyValue = styleDeclaration.getPropertyValue(property);
                    final var isImportant = "important".equals(styleDeclaration.getPropertyPriority(property));
                    properties.put(property, Tuples.of(propertyValue, new Priority(priority, isImportant)));
                }
            }
        }

        return parsedStyles;
    }
}
