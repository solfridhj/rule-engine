package no.shj.payment.ruleengine.rules;

import com.neovisionaries.i18n.CountryCode;
import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.context.PaymentRuleContext;
import no.shj.payment.ruleengine.context.RuleExecutionResult;
import no.shj.payment.ruleengine.database.RuleConfigurationRepository;
import no.shj.payment.ruleengine.generic.AbstractRule;
import no.shj.payment.ruleengine.generic.RuleMetadata;

@RuleMetadata(
    ruleId = Rule.PAYMENT_METHOD_FROM_COUNTRY,
    ruleDescription = "Selects payment methods based on the country")
public class PaymentMethodRule extends AbstractRule<String, Map<CountryCode, String>> {
  public PaymentMethodRule(
      RuleConfigurationRepository<Map<CountryCode, String>> configurationRepository) {
    super(configurationRepository);
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
    if (!paymentMethod.isEmpty()) {
      return Optional.of(paymentMethod);
    }
    return Optional.empty();
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setPaymentMethod(output);
  }
}
