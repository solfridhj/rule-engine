package no.shj.payment.ruleengine.rules;

import com.neovisionaries.i18n.CountryCode;
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
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("MASTERCARD")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount("fixme")
            .paymentCurrency("SEK")
            .build();

    ruleSet.evaluateRules(context);

    PaymentRuleContext context2 =
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("VISA")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount("fixme")
            .paymentCurrency("SEK")
            .build();
    ruleSet.evaluateRules(context2);
  }
}
