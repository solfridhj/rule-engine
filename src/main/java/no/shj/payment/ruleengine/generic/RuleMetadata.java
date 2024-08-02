package no.shj.payment.ruleengine.generic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime
@Target(ElementType.TYPE)
@Component
public @interface RuleMetadata {

  no.shj.payment.ruleengine.rules.Rule ruleId();

  String ruleDescription();
}
