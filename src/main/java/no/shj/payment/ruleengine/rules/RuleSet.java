package no.shj.payment.ruleengine.rules;

import no.shj.payment.ruleengine.context.PaymentRuleContext;
import org.springframework.stereotype.Component;

@Component
public class RuleSet {

  private final PaymentMethodRule paymentMethodRule;
  private final AcquirerRoutingRule acquirerRoutingRule;

  public RuleSet(PaymentMethodRule paymentMethodRule, AcquirerRoutingRule acquirerRoutingRule) {
    this.paymentMethodRule = paymentMethodRule;
    this.acquirerRoutingRule = acquirerRoutingRule;
  }

  public void evaluateRules(PaymentRuleContext context) {
    paymentMethodRule.executeRule(context);
    acquirerRoutingRule.executeRule(context);
    // Add new rules here
  }
}
