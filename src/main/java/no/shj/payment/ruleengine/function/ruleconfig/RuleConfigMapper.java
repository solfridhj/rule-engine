package no.shj.payment.ruleengine.function.ruleconfig;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.function.ruleconfig.response.ExistingRulesResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RuleConfigMapper {

  public Set<ExistingRulesResponseDto> map(Set<RuleConfigurationEntity> result) {
    return result.stream().map(this::map).collect(Collectors.toSet());
  }

  private ExistingRulesResponseDto map(RuleConfigurationEntity ruleConfiguration) {
    return new ExistingRulesResponseDto()
        .setId(ruleConfiguration.getId())
        .setRuleId(ruleConfiguration.getRuleId().toString())
        .setIsActive(ruleConfiguration.getIsActive())
        .setCreatedBy(ruleConfiguration.getCreatedBy())
        .setCreatedDate(
            ruleConfiguration.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .setRuleSpecificConfigurationData(ruleConfiguration.getRuleSpecificConfigurationData());
  }
}
