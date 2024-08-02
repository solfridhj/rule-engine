package no.shj.payment.ruleengine.generic;

import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.context.PaymentRuleContext;
import no.shj.payment.ruleengine.context.RuleExecutionInformation;
import no.shj.payment.ruleengine.context.RuleExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRule<OutputType> {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  protected abstract Map<String, Object> ruleInput(PaymentRuleContext context);

  protected abstract Optional<OutputType> ruleLogic(PaymentRuleContext context);

  protected abstract void setResult(RuleExecutionResult result, OutputType output);

  public final void executeRule(PaymentRuleContext context) {
    var metadata = this.getClass().getAnnotation(Rule.class);

    log.info(String.format("Executing rule with id [%s]", metadata.ruleId()));

    var ruleInputData = ruleInput(context);
    var output = ruleLogic(context);
    output.ifPresent(outputType -> setResult(context.getRuleExecutionResult(), outputType));

    var ruleExecutionMetadata =
        new RuleExecutionInformation()
            .setRuleInput(ruleInputData)
            .setRuleId(metadata.ruleId())
            .setRuleDescription(metadata.ruleDescription())
            .setWasTriggered(output.isPresent());
    context.getRuleExecutionInformationList().add(ruleExecutionMetadata);
  }
}
