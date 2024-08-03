package no.shj.payment.ruleengine.ruleservice.rules;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.FEE_CALCULATION;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionResult;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import org.javamoney.moneta.Money;

@RuleMetadata(ruleId = FEE_CALCULATION, ruleDescription = "Calculates fees for payments")
public class FeeCalculationRule
    extends AbstractRule<Money, Map<String, Map<String, Map<String, String>>>> {

  protected FeeCalculationRule(
      RuleConfigurationDaoImpl<Map<String, Map<String, Map<String, String>>>>
          ruleConfigurationDao) {
    super(ruleConfigurationDao);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of(
        "transactionAmount", context.getTransactionAmount(),
        "paymentCurrency", context.getPaymentCurrency(),
        "cardType", context.getCardType());
  }

  @Override
  protected Optional<Money> ruleLogic(
      PaymentRuleContext context, Map<String, Map<String, Map<String, String>>> config) {

    /*
    var config = Map.of("CREDIT",
                    Map.of("NOK", Pair.of("10000", "2"),
                            "SEK", Pair.of("20000", "4")));*/

    var cardType = context.getCardType();
    var transactionAmount = context.getTransactionAmount();
    var paymentCurrency = context.getPaymentCurrency();

    var cardTypeMap = config.get(cardType);
    if (cardTypeMap == null || cardTypeMap.isEmpty()) {
      // No fees for the given card type
      return Optional.empty();
    }
    var currencyMap = cardTypeMap.get(paymentCurrency);
    if (currencyMap == null) {
      // No fees for the currency
      return Optional.empty();
    }
    var firstEntrySet = currencyMap.entrySet().stream().findFirst().orElse(null);
    if (firstEntrySet == null) {
      return Optional.empty();
    }
    String minAmount = firstEntrySet.getKey();
    String fee = firstEntrySet.getValue();

    BigDecimal minLimitTransactionFee = new BigDecimal(minAmount);
    if (transactionAmountIsLessThanTransFeeMinLimit(transactionAmount, minLimitTransactionFee)) {
      BigDecimal transactionFeePercentage = new BigDecimal(fee);
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
}
