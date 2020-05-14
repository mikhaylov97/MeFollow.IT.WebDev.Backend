package com.mefollow.webschool.management.admin.usecase.TakeRules;

import com.mefollow.webschool.checker.domain.base.CheckRule;
import com.mefollow.webschool.checker.domain.base.CssCheckRule;
import com.mefollow.webschool.checker.domain.base.HtmlCheckRule;
import com.mefollow.webschool.checker.infrastructure.repository.CheckRuleRepository;
import com.mefollow.webschool.management.admin.application.PermissionService;
import com.mefollow.webschool.management.admin.domain.dto.RulesDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.mefollow.webschool.management.admin.domain.base.ManagementException.ACTION_IS_FORBIDDEN;
import static com.mefollow.webschool.management.admin.domain.base.PermissionLevel.FIRST_LEVEL;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Service
public class TakeRulesHandler {

    private final PermissionService permissionService;
    private final CheckRuleRepository checkRuleRepository;

    public TakeRulesHandler(PermissionService permissionService, CheckRuleRepository checkRuleRepository) {
        this.permissionService = permissionService;
        this.checkRuleRepository = checkRuleRepository;
    }

    public Mono<RulesDto> handle(TakeRulesCommand command) {
        return permissionService.checkPermission(command.getUserId(), FIRST_LEVEL)
                .flatMap(hasPermission -> hasPermission
                        ? processHandling(command)
                        : error(ACTION_IS_FORBIDDEN));
    }

    private Mono<RulesDto> processHandling(TakeRulesCommand command) {
        return checkRuleRepository.findAllByChapterId(command.getChapterId())
                .collectList()
                .map(rules -> {
                    var htmlRulesCount = 1;
                    var cssRulesCount = 1;
                    var jsRulesCount = 1;
                    final var dto = new RulesDto();
                    for (CheckRule rule : rules) {
                        switch (rule.getType()) {
                            case HTML:
                                dto.addHtmlRule(rule.getId(), ((HtmlCheckRule) rule).getCssSelector(), htmlRulesCount++);
                                break;
                            case CSS:
                                final var count = cssRulesCount++;
                                rules.stream().filter(r -> r.getId().equals(((CssCheckRule) rule).getHtmlCheckRuleId())).findFirst().ifPresent(r -> {
                                    dto.addCssRule(rule.getId(), format("CSS-правило для [%s]", ((HtmlCheckRule) r).getCssSelector()), count);
                                });
                                break;
                            case JS:
                                dto.addJsRule(rule.getId(), "JS-правило", jsRulesCount++);
                                break;
                        }
                    }

                    return dto;
                });
    }
}
