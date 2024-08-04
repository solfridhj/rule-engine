package no.shj.payment.ruleengine.ruleservice.rules;

import com.neovisionaries.i18n.CountryCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    ruleId = Rule.PAYMENT_METHOD_FROM_COUNTRY,
    ruleDescription = "Selects payment methods based on the country",
    version = 1)
public class PaymentMethodRule extends AbstractRule<String, PaymentMethodRule.ConfigStructure> {

  public PaymentMethodRule(RuleConfigurationDaoImpl ruleConfigurationDao) {
    super(ruleConfigurationDao, ConfigStructure.class);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of("paymentOriginCountry", context.getPaymentOriginCountry());
  }

  @Override
  protected Optional<String> ruleLogic(
      PaymentRuleContext context, PaymentMethodRule.ConfigStructure config) {
    CountryCode paymentOriginCountry = context.getPaymentOriginCountry();
    var paymentMapping =
        config.getPaymentMethodMappings().stream()
            .filter(
                paymentMethodMapping ->
                    paymentMethodMapping.getPaymentOriginCountryCode().equals(paymentOriginCountry))
            .findFirst()
            .orElse(null);

    if (paymentMapping == null) {
      return Optional.empty();
    }
    return Optional.of(paymentMapping.getPaymentMethod());
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setPaymentMethod(output);
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class ConfigStructure {
    private @NotNull List<@NotNull @Valid PaymentMethodMapping> paymentMethodMappings;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class PaymentMethodMapping {
    private @NotNull @Valid CountryCode paymentOriginCountryCode;
    private @NotBlank @Size(min = 2, max = 30) @Pattern(
        regexp = "^[a-zA-Z0-9 ]*$",
        message = "Field can only contain letters, spaces, and numbers") String paymentMethod;
  }
}
