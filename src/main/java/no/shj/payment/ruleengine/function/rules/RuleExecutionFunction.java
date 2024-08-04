package no.shj.payment.ruleengine.function.rules;

import java.util.function.Function;
import no.shj.payment.ruleengine.function.rules.mapper.ContextDtoMapper;
import no.shj.payment.ruleengine.function.rules.request.RuleEngineRequestDto;
import no.shj.payment.ruleengine.function.rules.response.RuleEngineResponseDto;
import no.shj.payment.ruleengine.ruleservice.RuleSetExecutionService;
import org.springframework.stereotype.Component;

// Note - starting point here - could also have mapped to the context at this point.
//  Intended to be portable so I can use this for a Lambda in the future.
@Component
public class RuleExecutionFunction
    implements Function<RuleEngineRequestDto, RuleEngineResponseDto> {

  private final RuleSetExecutionService ruleSetExecutionService;
  private final ContextDtoMapper mapper;

  public RuleExecutionFunction(
      RuleSetExecutionService ruleSetExecutionService, ContextDtoMapper mapper) {
    this.ruleSetExecutionService = ruleSetExecutionService;
    this.mapper = mapper;
  }

  @Override
  public RuleEngineResponseDto apply(RuleEngineRequestDto ruleEngineRequestDto) {
    var context = mapper.map(ruleEngineRequestDto);
    ruleSetExecutionService.evaluateRules(context); // Context data is updated in the rule-set.
    return mapper.map(context);
  }
}
