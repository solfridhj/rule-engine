package no.shj.payment.ruleengine.function.ruleconfig;

import java.util.List;
import java.util.function.Function;
import no.shj.payment.ruleengine.configservice.RuleConfigService;
import no.shj.payment.ruleengine.function.ruleconfig.response.ExistingRulesResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RuleConfigExecutionFunction implements Function<Void, List<ExistingRulesResponseDto>> {

  private final RuleConfigService service;
  private final RuleConfigMapper mapper;

  public RuleConfigExecutionFunction(RuleConfigService service, RuleConfigMapper mapper) {
    this.service = service;
    this.mapper = mapper;
  }

  @Override
  public List<ExistingRulesResponseDto> apply(Void unused) {
    var result = service.getAllRulesAndConfig();
    return mapper.map(result);
  }
}
