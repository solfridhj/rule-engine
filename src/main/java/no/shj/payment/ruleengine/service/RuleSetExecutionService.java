package no.shj.payment.ruleengine.service;

import no.shj.payment.ruleengine.service.context.PaymentRuleContext;
import no.shj.payment.ruleengine.service.rules.AcquirerRoutingRule;
import no.shj.payment.ruleengine.service.rules.FeeCalculationRule;
import no.shj.payment.ruleengine.service.rules.PaymentMethodRule;
import org.springframework.stereotype.Component;

@Component
public class RuleSetExecutionService {

  private final PaymentMethodRule paymentMethodRule;
  private final AcquirerRoutingRule acquirerRoutingRule;
  private final FeeCalculationRule feeCalculationRule;

  public RuleSetExecutionService(
      PaymentMethodRule paymentMethodRule,
      AcquirerRoutingRule acquirerRoutingRule,
      FeeCalculationRule feeCalculationRule) {
    this.paymentMethodRule = paymentMethodRule;
    this.acquirerRoutingRule = acquirerRoutingRule;
    this.feeCalculationRule = feeCalculationRule;
  }

  public void evaluateRules(PaymentRuleContext context) {
    paymentMethodRule.executeRule(context);
    acquirerRoutingRule.executeRule(context);
    feeCalculationRule.executeRule(context);
    // Add new rules here
  }
}
