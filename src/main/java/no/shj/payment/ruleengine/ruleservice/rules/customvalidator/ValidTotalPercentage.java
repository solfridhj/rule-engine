package no.shj.payment.ruleengine.ruleservice.rules.customvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TotalPercentageValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTotalPercentage {
  String message() default "Total percentage must be 100";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
