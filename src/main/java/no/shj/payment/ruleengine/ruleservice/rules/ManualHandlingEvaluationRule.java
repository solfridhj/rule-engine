package no.shj.payment.ruleengine.ruleservice.rules;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.MANUAL_HANDLING_DUE_TO_AMOUNT_EVALUATION;

import com.neovisionaries.i18n.CountryCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionResult;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;

@RuleMetadata(
    ruleId = MANUAL_HANDLING_DUE_TO_AMOUNT_EVALUATION,
    ruleDescription = "Evaluates if manual handling is required",
    version = 1)
public class ManualHandlingEvaluationRule
    extends AbstractRule<Boolean, ManualHandlingEvaluationRule.RuleConfig> {

  public ManualHandlingEvaluationRule(RuleConfigurationDaoImpl ruleConfigurationDao) {
    super(ruleConfigurationDao, RuleConfig.class);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of(
        "paymentOriginCountry", context.getPaymentOriginCountry(),
        "paymentCurrency", context.getPaymentCurrency(),
        "transactionAmount", context.getTransactionAmount());
  }

  @Override
  protected Optional<Boolean> ruleLogic(PaymentRuleContext context, RuleConfig ruleSpecificConfig) {

    var paymentOriginCountry = context.getPaymentOriginCountry();
    var paymentCurrency = context.getPaymentCurrency();
    var transactionAmount = context.getTransactionAmount();

    var countryMapping =
        ruleSpecificConfig.getCountryMappings().stream()
            .filter(mapping -> mapping.getCountryCode().equals(paymentOriginCountry))
            .findFirst()
            .orElse(null);
    if (countryMapping == null) {
      return Optional.empty();
    }

    var currencyMapping =
        countryMapping.getCurrencyMappings().stream()
            .filter(mapping -> mapping.getCurrency().equals(paymentCurrency))
            .findFirst()
            .orElse(null);

    if (currencyMapping == null) {
      return Optional.empty();
    }

    var amountTriggeringManualHandling = currencyMapping.getAmountTriggeringManualHandling();
    if (transactionAmountIsGreaterThanAmountTriggeringManualHandling(
        transactionAmount, amountTriggeringManualHandling)) {
      return Optional.of(Boolean.TRUE);
    }

    return Optional.empty();
  }

  private static boolean transactionAmountIsGreaterThanAmountTriggeringManualHandling(
      BigDecimal transactionAmount, BigDecimal amountTriggeringManualHandling) {
    return transactionAmount.compareTo(amountTriggeringManualHandling) > 0;
  }

  @Override
  protected void setResult(RuleExecutionResult result, Boolean output) {
    result.setManualVerificationRequired(output);
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class RuleConfig {
    private @NotNull List<@NotNull @Valid CountryMapping> countryMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class CountryMapping {
    private @NotNull CountryCode countryCode;
    private @NotEmpty List<@NotNull @Valid CurrencyMapping> currencyMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class CurrencyMapping {
    private @NotNull @Size(min = 3, max = 3) String currency;
    private @NotNull BigDecimal amountTriggeringManualHandling;
  }
}
