package no.shj.payment.ruleengine.rules;

import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.context.PaymentRuleContext;
import no.shj.payment.ruleengine.context.RuleExecutionResult;
import no.shj.payment.ruleengine.generic.AbstractRule;
import no.shj.payment.ruleengine.generic.Rule;

@Rule(
    ruleId = "PAYMENT_METHOD_FROM_COUNTRY",
    ruleDescription = "Selects payment methods based on the country")
public class PaymentMethodRule extends AbstractRule<String> {

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of("paymentOriginCountry", context.getPaymentOriginCountry());
  }

  @Override
  protected Optional<String> ruleLogic(PaymentRuleContext context) {
    String paymentOriginCountry = context.getPaymentOriginCountry();
    // FIXME - Load from configuration
    if (paymentOriginCountry.equalsIgnoreCase("norway")) {
      return Optional.of("Vipps");
    }

    return Optional.empty();
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setPaymentMethod(output);
  }
}
