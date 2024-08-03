package no.shj.payment.ruleengine.service;

import no.shj.payment.ruleengine.service.context.PaymentRuleContext;
import no.shj.payment.ruleengine.service.rules.AcquirerRoutingRule;
import no.shj.payment.ruleengine.service.rules.PaymentMethodRule;
import org.springframework.stereotype.Component;

@Component
public class RuleSetExecutionService {

  private final PaymentMethodRule paymentMethodRule;
  private final AcquirerRoutingRule acquirerRoutingRule;

  public RuleSetExecutionService(
      PaymentMethodRule paymentMethodRule, AcquirerRoutingRule acquirerRoutingRule) {
    this.paymentMethodRule = paymentMethodRule;
    this.acquirerRoutingRule = acquirerRoutingRule;
  }

  public void evaluateRules(PaymentRuleContext context) {
    paymentMethodRule.executeRule(context);
    acquirerRoutingRule.executeRule(context);
    // Add new rules here
  }
}
