package no.shj.payment.ruleengine.service.rules;

import com.neovisionaries.i18n.CountryCode;
import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.service.context.PaymentRuleContext;
import no.shj.payment.ruleengine.service.context.RuleExecutionResult;
import no.shj.payment.ruleengine.service.genericengine.AbstractRule;
import no.shj.payment.ruleengine.service.genericengine.RuleMetadata;

@RuleMetadata(
    ruleId = Rule.PAYMENT_METHOD_FROM_COUNTRY,
    ruleDescription = "Selects payment methods based on the country")
public class PaymentMethodRule extends AbstractRule<String, Map<CountryCode, String>> {

  public PaymentMethodRule(
      RuleConfigurationDaoImpl<Map<CountryCode, String>> ruleConfigurationDao) {
    super(ruleConfigurationDao);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of("paymentOriginCountry", context.getPaymentOriginCountry());
  }

  @Override
  protected Optional<String> ruleLogic(
      PaymentRuleContext context, Map<CountryCode, String> config) {
    CountryCode paymentOriginCountry = context.getPaymentOriginCountry();
    String paymentMethod =
        config.get(paymentOriginCountry.getAlpha2()); // Works although IntelliJ complains
    if (paymentMethod != null && !paymentMethod.isEmpty()) {
      return Optional.of(paymentMethod);
    }
    return Optional.empty();
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setPaymentMethod(output);
  }
}
