package no.shj.payment.ruleengine.ruleservice.genericengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionInformation;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract rule for which all rules <b>must</b> extend, in addition to the rule having the {@link
 * RuleMetadata} annotation.
 *
 * @param <T> Type of the output of the result of the rule execution. Used to separate the execution
 *     logic from update logic.
 * @param <S> Type of the configuration data. Needs to be a class defined either as static in the
 *     rule or elsewhere.
 */
public abstract class AbstractRule<T, S> {

  private final Class<S> type;
  private final RuleConfigurationDaoImpl ruleConfigurationDao;
  private final ObjectMapper objectMapper = new ObjectMapper();

  protected AbstractRule(RuleConfigurationDaoImpl ruleConfigurationDao, Class<S> type) {
    this.ruleConfigurationDao = ruleConfigurationDao;
    this.type = type;
  }

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  protected abstract Map<String, Object> ruleInput(PaymentRuleContext context);

  protected abstract Optional<T> ruleLogic(PaymentRuleContext context, S ruleSpecificConfig);

  protected abstract void setResult(RuleExecutionResult result, T output);

  public final void executeRule(PaymentRuleContext context) {
    var metadata = this.getClass().getAnnotation(RuleMetadata.class);
    var ruleId = metadata.ruleId();
    var ruleVersion = metadata.version();

    var ruleConfigurationOptional =
        ruleConfigurationDao.getRuleConfigurationEntity(ruleId, ruleVersion);
    if (ruleConfigurationOptional.isEmpty()) {
      // Rule does not have configuration - will be treated as if it's inactive.
      log.warn(
          "Not executing rule with id {} as the rule has no configuration for version {}.",
          ruleId,
          ruleVersion);
      return;
    }
    var ruleConfiguration = ruleConfigurationOptional.get();
    if (!ruleConfiguration.getIsActive()) {
      log.trace("Not executing rule with id {} as the rule is inactivated.", ruleId);
      return;
    }

    log.trace("Executing rule with id {}", ruleId);

    var ruleInputData = ruleInput(context);

    log.trace("Rule id {} input: {}", ruleId, ruleInputData);

    log.trace(
        "Rule id {} has config: {}", ruleId, ruleConfiguration.getRuleSpecificConfigurationData());

    S configOnObjectFormat =
        objectMapper.convertValue(ruleConfiguration.getRuleSpecificConfigurationData(), type);
    Optional<T> outputOptional = ruleLogic(context, configOnObjectFormat);
    outputOptional.ifPresent(outputType -> setResult(context.getRuleExecutionResult(), outputType));
    var ruleExecutionMetadata =
        new RuleExecutionInformation()
            .setRuleInput(ruleInputData)
            .setRuleId(ruleId)
            .setRuleDescription(metadata.ruleDescription())
            .setWasTriggered(outputOptional.isPresent());

    outputOptional.ifPresent(
        t -> log.trace("Rule with id {} was triggered, with result: {}", ruleId, t));
    context.getRuleExecutionInformationList().add(ruleExecutionMetadata);
  }
}
