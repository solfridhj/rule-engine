package no.shj.payment.ruleengine.rules;

import static no.shj.payment.ruleengine.rules.Rule.ACQUIRER_ROUTING;

import java.math.BigDecimal;
import java.util.*;
import no.shj.payment.ruleengine.context.PaymentRuleContext;
import no.shj.payment.ruleengine.context.RuleExecutionResult;
import no.shj.payment.ruleengine.database.RuleConfigurationRepository;
import no.shj.payment.ruleengine.generic.AbstractRule;
import no.shj.payment.ruleengine.generic.RuleMetadata;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

@RuleMetadata(
    ruleId = ACQUIRER_ROUTING,
    ruleDescription = "Determines what acquirer to route the payment to")
public class AcquirerRoutingRule
    extends AbstractRule<String, Map<String, Map<String, Map<String, String>>>> {

  public AcquirerRoutingRule(
      RuleConfigurationRepository<Map<String, Map<String, Map<String, String>>>>
          configurationRepository) {
    super(configurationRepository);
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

    // Pretend these are configurable from somewhere fetched from e.g a database.
    // Assuming configuration is correct and all probabilities add up. Should verify when app loads
    // config.
    Map<String, Map<String, Map<String, BigDecimal>>> ruleConfiguration =
        new HashMap<>(
            Map.of(
                "SEK",
                Map.of(
                    "MASTERCARD",
                    Map.of(
                        "Acquirer A",
                        BigDecimal.valueOf(20.00),
                        "Acquirer B",
                        BigDecimal.valueOf(50.00),
                        "Acquirer C",
                        BigDecimal.valueOf(30.00)))));
    var paymentMethod = context.getPaymentMethod();
    var paymentCurrency = context.getPaymentCurrency();
    var mapForCurrency = config.get(paymentCurrency);
    if (mapForCurrency.isEmpty()) {
      // Acquirer is not changed for the provided currency.
      return Optional.empty();
    }

    Map<String, String> acquirerAndProbability = mapForCurrency.get(paymentMethod);
    if (acquirerAndProbability.isEmpty()) {
      // Combination of currency and payment method does not lead to change in acquirer.
      return Optional.empty();
    }
    return Optional.of(getRandomValue(acquirerAndProbability));
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setAcquirerId(output);
  }

  // TODO - have to test this a bit as I haven't used it before
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
