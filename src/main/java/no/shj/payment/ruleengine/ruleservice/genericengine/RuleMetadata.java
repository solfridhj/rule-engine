package no.shj.payment.ruleengine.ruleservice.genericengine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RuleMetadata {

  Rule ruleId();

  String ruleDescription();

  int version();
}
