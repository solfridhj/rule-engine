package no.shj.payment.ruleengine.ruleservice.rules;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.ACQUIRER_ROUTING;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionResult;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.customvalidator.ValidTotalPercentage;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

@RuleMetadata(
    ruleId = ACQUIRER_ROUTING,
    ruleDescription = "Determines what acquirer to route the payment to",
    version = 1)
public class AcquirerRoutingRule extends AbstractRule<String, AcquirerRoutingRule.ConfigStructure> {

  public AcquirerRoutingRule(RuleConfigurationDaoImpl ruleConfigurationDao) {
    super(ruleConfigurationDao, ConfigStructure.class);
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
      PaymentRuleContext context, AcquirerRoutingRule.ConfigStructure config) {
    var paymentMethod = context.getPaymentMethod();
    var paymentCurrency = context.getPaymentCurrency();

    var currencyMapping =
        config.getCurrencyMappings().stream()
            .filter(mapping -> mapping.getPaymentCurrency().equals(paymentCurrency))
            .findFirst()
            .orElse(null);
    if (currencyMapping == null) {
      // Acquirer is not changed for the provided currency.
      return Optional.empty();
    }
    var paymentMethodMapping =
        currencyMapping.getPaymentMethodMappings().stream()
            .filter(mapping -> mapping.getPaymentMethod().equals(paymentMethod))
            .findFirst()
            .orElse(null);

    if (paymentMethodMapping == null) {
      // Acquirer is not changed for the provided payment method.
      return Optional.empty();
    }

    Map<String, BigDecimal> acquirerAndProbability =
        paymentMethodMapping.getAcquirerRoutingMappings().stream()
            .collect(
                Collectors.toMap(
                    AcquirerRoutingMapping::getAcquirerName,
                    AcquirerRoutingMapping::getPercentageRoutedTo));
    if (acquirerAndProbability.isEmpty()) {
      // Combination of currency and payment method does not lead to change in acquirer.
      return Optional.empty();
    }
    return Optional.of(getRandomAcquirer(acquirerAndProbability));
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setAcquirerId(output);
  }

  private static String getRandomAcquirer(Map<String, BigDecimal> map) {
    EnumeratedDistribution<String> distribution =
        new EnumeratedDistribution<>(convertToPairList(map));
    return distribution.sample();
  }

  private static List<Pair<String, Double>> convertToPairList(Map<String, BigDecimal> map) {
    List<Pair<String, Double>> pairs = new ArrayList<>();
    for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
      pairs.add(new Pair<>(entry.getKey(), entry.getValue().doubleValue()));
    }
    return pairs;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class ConfigStructure {
    private @NotNull List<@NotNull @Valid CurrencyMapping> currencyMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class CurrencyMapping {
    private @NotBlank @Size(min = 3, max = 3) @Pattern(
        regexp = "^[A-Z]+$",
        message = "Field can only contain capitalized letters") String paymentCurrency;
    private @NotEmpty List<@NotNull @Valid PaymentMethodMapping> paymentMethodMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  @ValidTotalPercentage
  public static class PaymentMethodMapping {
    private @NotBlank @Size(min = 3, max = 25) @Pattern(
        regexp = "^[a-zA-Z0-9 ]*$",
        message = "Field can only contain letters, spaces, and numbers") String paymentMethod;
    private @NotEmpty List<@NotNull @Valid AcquirerRoutingMapping> acquirerRoutingMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class AcquirerRoutingMapping {
    private @NotBlank @Size(min = 1, max = 50) @Pattern(
        regexp = "^[a-zA-Z0-9 ]*$",
        message = "Field can only contain letters, spaces, and numbers") String acquirerName;
    private @NotNull @DecimalMin(value = "0.0") @DecimalMax(value = "100.0") BigDecimal
        percentageRoutedTo;
  }
}
