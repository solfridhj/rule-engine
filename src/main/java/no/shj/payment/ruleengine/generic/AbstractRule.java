package no.shj.payment.ruleengine.generic;

import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.context.PaymentRuleContext;
import no.shj.payment.ruleengine.context.RuleExecutionInformation;
import no.shj.payment.ruleengine.context.RuleExecutionResult;
import no.shj.payment.ruleengine.database.RuleConfigurationRepository;
import no.shj.payment.ruleengine.exception.PaymentRuleEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRule<T, S> {

  // TODO - see if this can be the specific object structure deserialization.
  private final RuleConfigurationRepository<S> configurationRepository;

  protected AbstractRule(RuleConfigurationRepository<S> configurationRepository) {
    this.configurationRepository = configurationRepository;
  }

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  protected abstract Map<String, Object> ruleInput(PaymentRuleContext context);

  protected abstract Optional<T> ruleLogic(PaymentRuleContext context, S ruleSpecificConfig);

  protected abstract void setResult(RuleExecutionResult result, T output);

  public final void executeRule(PaymentRuleContext context) {
    var metadata = this.getClass().getAnnotation(RuleMetadata.class);
    var ruleId = metadata.ruleId();

    var ruleConfigurationOptional = configurationRepository.findByRuleId(ruleId);
    if (ruleConfigurationOptional.isEmpty()) {
      throw new PaymentRuleEngineException(
          String.format("Rule id %s is missing configuration", ruleId));
    }
    var ruleConfiguration = ruleConfigurationOptional.get();
    if (!ruleConfiguration.isActive()) {
      log.info(String.format("Not executing rule with id %s as the rule is inactivated.", ruleId));
      return;
    }

    log.info(String.format("Executing rule with id %s", ruleId));

    var ruleInputData = ruleInput(context);
    log.debug(String.format("Rule id %s input: %s", ruleId, ruleInputData));

    log.debug(
        String.format(
            "Rule id %s has config: %s",
            ruleId, ruleConfiguration.getRuleSpecificConfigurationData()));
    var output = ruleLogic(context, ruleConfiguration.getRuleSpecificConfigurationData());
    output.ifPresent(outputType -> setResult(context.getRuleExecutionResult(), outputType));
    var ruleExecutionMetadata =
        new RuleExecutionInformation()
            .setRuleInput(ruleInputData)
            .setRuleId(ruleId)
            .setRuleDescription(metadata.ruleDescription())
            .setWasTriggered(output.isPresent());
    if (ruleExecutionMetadata.wasTriggered()) {
      log.debug(String.format("Rule id %s has result: %s", ruleId, output.get()));
      log.info(String.format("Rule with id %s was triggered", ruleId));
    }
    context.getRuleExecutionInformationList().add(ruleExecutionMetadata);
  }
}
