package no.shj.payment.ruleengine.function.ruleconfig;

import java.util.List;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.function.ruleconfig.response.ExistingRulesResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RuleConfigMapper {

  public List<ExistingRulesResponseDto> map(List<RuleConfigurationEntity<Object>> result) {
    return result.stream().map(this::map).toList();
  }

  private ExistingRulesResponseDto map(RuleConfigurationEntity<Object> ruleConfiguration) {
    return new ExistingRulesResponseDto()
        .setId(ruleConfiguration.getId())
        .setRuleId(ruleConfiguration.getRuleId().toString())
        .setIsActive(ruleConfiguration.isActive())
        .setRuleSpecificConfigurationData(ruleConfiguration.getRuleSpecificConfigurationData());
  }
}
