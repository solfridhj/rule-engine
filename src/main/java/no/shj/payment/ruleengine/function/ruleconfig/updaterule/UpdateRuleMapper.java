package no.shj.payment.ruleengine.function.ruleconfig.updaterule;

import java.time.format.DateTimeFormatter;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import org.springframework.stereotype.Component;

@Component
public class UpdateRuleMapper {

  public RuleConfigurationEntity map(UpdateRuleConfigurationRequestDto requestDto) {
    return new RuleConfigurationEntity()
        .setRuleId(requestDto.getRuleId())
        .setIsActive(requestDto.getIsActive())
        .setRuleSpecificConfigurationData(requestDto.getRuleSpecificConfigurationData());
  }

  public UpdateRuleConfigurationResponseDto map(RuleConfigurationEntity entity) {
    return new UpdateRuleConfigurationResponseDto()
        .setRuleId(entity.getRuleId())
        .setIsActive(entity.getIsActive())
        .setCreatedBy(entity.getCreatedBy())
        .setCreatedDate(entity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        .setRuleSpecificConfigurationData(entity.getRuleSpecificConfigurationData());
  }
}
