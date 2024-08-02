package no.shj.payment.ruleengine.rules;

import no.shj.payment.ruleengine.context.PaymentRuleContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RuleSetTest {

  @Autowired private RuleSet ruleSet;

  @Test
  void integrationTest() {
    PaymentRuleContext context =
        PaymentRuleContext.PaymentRuleContextBuilder.Builder()
            .customerType("EMPLOYEE")
            .paymentMethod("MC")
            .paymentOriginCountry("NORWAY")
            .build();

    ruleSet.evaluateRules(context);
  }
}
