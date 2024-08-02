package no.shj.payment.ruleengine.rules;

import no.shj.payment.ruleengine.context.PaymentRuleContext;
import org.springframework.stereotype.Component;

@Component
public class RuleSet {

  private final PaymentMethodRule paymentMethodRule;

  public RuleSet(PaymentMethodRule paymentMethodRule) {
    this.paymentMethodRule = paymentMethodRule;
  }

  public void evaluateRules(PaymentRuleContext context) {
    paymentMethodRule.executeRule(context);
    // Add new rules here
  }
}
