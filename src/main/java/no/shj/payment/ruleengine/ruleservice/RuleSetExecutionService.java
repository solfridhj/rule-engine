package no.shj.payment.ruleengine.ruleservice;

import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.rules.AcquirerRoutingRule;
import no.shj.payment.ruleengine.ruleservice.rules.FeeCalculationRule;
import no.shj.payment.ruleengine.ruleservice.rules.PaymentMethodRule;
import org.springframework.stereotype.Service;

@Service
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
  }
}
