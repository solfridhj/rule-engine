package no.shj.payment.ruleengine.function.ruleconfig;

import java.util.function.Function;
import no.shj.payment.ruleengine.configservice.RuleConfigService;
import no.shj.payment.ruleengine.function.ruleconfig.updaterule.UpdateRuleConfigurationRequestDto;
import no.shj.payment.ruleengine.function.ruleconfig.updaterule.UpdateRuleConfigurationResponseDto;
import no.shj.payment.ruleengine.function.ruleconfig.updaterule.UpdateRuleMapper;
import org.springframework.stereotype.Component;

@Component
public class UpdateRuleExecutionFunction
    implements Function<UpdateRuleConfigurationRequestDto, UpdateRuleConfigurationResponseDto> {

  UpdateRuleMapper mapper;
  RuleConfigService service;

  public UpdateRuleExecutionFunction(UpdateRuleMapper mapper, RuleConfigService service) {
    this.mapper = mapper;
    this.service = service;
  }

  @Override
  public UpdateRuleConfigurationResponseDto apply(
      UpdateRuleConfigurationRequestDto updateRuleConfigurationRequestDto) {
    var entity = mapper.map(updateRuleConfigurationRequestDto);
    var createdEntity = service.saveRuleConfiguration(entity);
    return mapper.map(createdEntity);
  }
}
