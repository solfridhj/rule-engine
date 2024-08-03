package no.shj.payment.ruleengine.ruleservice.rules;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.ACQUIRER_ROUTING;

import java.util.*;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionResult;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

@RuleMetadata(
    ruleId = ACQUIRER_ROUTING,
    ruleDescription = "Determines what acquirer to route the payment to")
public class AcquirerRoutingRule
    extends AbstractRule<String, Map<String, Map<String, Map<String, String>>>> {

  public AcquirerRoutingRule(
      RuleConfigurationDaoImpl<Map<String, Map<String, Map<String, String>>>>
          ruleConfigurationDao) {
    super(ruleConfigurationDao);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of(
        "paymentMethod",
        context.getPaymentMethod(),
        "paymentCurrency",
        context.getPaymentCurrency());
  }

  @Override
  protected Optional<String> ruleLogic(
      PaymentRuleContext context, Map<String, Map<String, Map<String, String>>> config) {

    var paymentMethod = context.getPaymentMethod();
    var paymentCurrency = context.getPaymentCurrency();
    var mapForCurrency = config.get(paymentCurrency);
    if (mapForCurrency == null || mapForCurrency.isEmpty()) {
      // Acquirer is not changed for the provided currency.
      return Optional.empty();
    }

    Map<String, String> acquirerAndProbability = mapForCurrency.get(paymentMethod);
    if (acquirerAndProbability == null || acquirerAndProbability.isEmpty()) {
      // Combination of currency and payment method does not lead to change in acquirer.
      return Optional.empty();
    }
    return Optional.of(getRandomValue(acquirerAndProbability));
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setAcquirerId(output);
  }

  private static <T> T getRandomValue(Map<T, String> map) {
    EnumeratedDistribution<T> distribution = new EnumeratedDistribution<>(convertToPairList(map));
    return distribution.sample();
  }

  private static <T> List<Pair<T, Double>> convertToPairList(Map<T, String> map) {
    List<Pair<T, Double>> pairs = new ArrayList<>();
    for (Map.Entry<T, String> entry : map.entrySet()) {
      pairs.add(new Pair<>(entry.getKey(), Double.parseDouble(entry.getValue())));
    }
    return pairs;
  }
}
