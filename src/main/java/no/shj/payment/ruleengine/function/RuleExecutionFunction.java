package no.shj.payment.ruleengine.function;

import java.util.function.Function;
import no.shj.payment.ruleengine.function.mapper.ContextDtoMapper;
import no.shj.payment.ruleengine.function.request.RuleEngineRequestDto;
import no.shj.payment.ruleengine.function.response.RuleEngineResponseDto;
import no.shj.payment.ruleengine.rules.RuleSet;
import org.springframework.stereotype.Component;

// Note - starting point here - could also have mapped to the context at this point.
//  Intended to be portable so I can use this for a Lambda in the future.
@Component
public class RuleExecutionFunction
    implements Function<RuleEngineRequestDto, RuleEngineResponseDto> {

  private final RuleSet ruleSet;
  private final ContextDtoMapper mapper;

  public RuleExecutionFunction(RuleSet ruleSet, ContextDtoMapper mapper) {
    this.ruleSet = ruleSet;
    this.mapper = mapper;
  }

  @Override
  public RuleEngineResponseDto apply(RuleEngineRequestDto ruleEngineRequestDto) {
    var context = mapper.map(ruleEngineRequestDto);
    ruleSet.evaluateRules(context); // Context data is updated in the rule-set.
    return mapper.map(context);
  }
}
