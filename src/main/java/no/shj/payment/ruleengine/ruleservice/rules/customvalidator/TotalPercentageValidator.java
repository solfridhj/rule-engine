package no.shj.payment.ruleengine.ruleservice.rules.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.List;
import no.shj.payment.ruleengine.ruleservice.rules.AcquirerRoutingRule;

public class TotalPercentageValidator
    implements ConstraintValidator<ValidTotalPercentage, AcquirerRoutingRule.PaymentMethodMapping> {

  @Override
  public boolean isValid(
      AcquirerRoutingRule.PaymentMethodMapping paymentMethodMapping,
      ConstraintValidatorContext context) {

    List<AcquirerRoutingRule.AcquirerRoutingMapping> acquirerRoutingMappings =
        paymentMethodMapping.getAcquirerRoutingMappings();
    BigDecimal totalPercentage =
        acquirerRoutingMappings.stream()
            .map(AcquirerRoutingRule.AcquirerRoutingMapping::getPercentageRoutedTo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return totalPercentage.compareTo(BigDecimal.valueOf(100)) == 0;
  }
}
