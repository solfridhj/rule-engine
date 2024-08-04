package no.shj.payment.ruleengine.ruleservice.rules;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.FEE_CALCULATION;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.javamoney.moneta.Money;

@RuleMetadata(
    ruleId = FEE_CALCULATION,
    ruleDescription = "Calculates fees for payments",
    version = 1)
public class FeeCalculationRule extends AbstractRule<Money, FeeCalculationRule.ConfigStructure> {

  public FeeCalculationRule(RuleConfigurationDaoImpl ruleConfigurationDao) {
    super(ruleConfigurationDao, ConfigStructure.class);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of(
        "transactionAmount", context.getTransactionAmount(),
        "paymentCurrency", context.getPaymentCurrency(),
        "cardType", context.getCardType());
  }

  @Override
  protected Optional<Money> ruleLogic(PaymentRuleContext context, ConfigStructure config) {

    var cardType = context.getCardType();
    var transactionAmount = context.getTransactionAmount();
    var paymentCurrency = context.getPaymentCurrency();

    var cardTypeMappings =
        config.getCardTypeMappings().stream()
            .filter(mapping -> mapping.getCardType().equals(cardType))
            .findFirst()
            .orElse(null);

    if (cardTypeMappings == null) {
      return Optional.empty();
    }

    var currencyMapping =
        cardTypeMappings.getCurrencyMappings().stream()
            .filter(mapping -> mapping.getCurrency().equals(paymentCurrency))
            .findFirst()
            .orElse(null);
    if (currencyMapping == null) {
      return Optional.empty();
    }

    BigDecimal minLimitTransactionFee = currencyMapping.getMinAmount();
    if (transactionAmountIsLessThanTransFeeMinLimit(transactionAmount, minLimitTransactionFee)) {
      BigDecimal transactionFeePercentage = currencyMapping.getFeePercentage();
      BigDecimal transactionFeeDecimal =
          transactionFeePercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN);
      BigDecimal calculatedFee = transactionAmount.multiply(transactionFeeDecimal);
      return Optional.of(Money.of(calculatedFee, context.getPaymentCurrency()));
    }

    return Optional.empty();
  }

  private boolean transactionAmountIsLessThanTransFeeMinLimit(
      BigDecimal transactionAmount, BigDecimal minLimitTransactionFee) {
    return transactionAmount.compareTo(minLimitTransactionFee) < 0;
  }

  @Override
  protected void setResult(RuleExecutionResult result, Money output) {
    result.setCalculatedFee(output);
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class ConfigStructure {
    private @NotNull List<@NotNull @Valid CardTypeMapping> cardTypeMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class CardTypeMapping {
    private @NotBlank @Size(min = 3, max = 35) @Pattern(
        regexp = "^[a-zA-Z0-9 ]*$",
        message = "Field can only contain letters, spaces, and numbers") String cardType;
    private @NotEmpty List<@NotNull @Valid CurrencyMapping> currencyMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class CurrencyMapping {
    private @NotBlank @Size(min = 3, max = 3) @Pattern(
        regexp = "^[A-Z]+$",
        message = "Field can only contain capitalized letters") String currency;
    private @NotNull @DecimalMin(value = "0.0") BigDecimal minAmount;
    private @NotNull @DecimalMin(value = "0.0") BigDecimal feePercentage;
  }
}
