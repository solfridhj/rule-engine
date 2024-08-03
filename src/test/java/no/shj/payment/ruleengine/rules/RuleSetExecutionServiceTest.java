package no.shj.payment.ruleengine.rules;

import com.neovisionaries.i18n.CountryCode;
import no.shj.payment.ruleengine.service.RuleSetExecutionService;
import no.shj.payment.ruleengine.service.context.PaymentRuleContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RuleSetExecutionServiceTest {

  @Autowired private RuleSetExecutionService ruleSetExecutionService;

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

    ruleSetExecutionService.evaluateRules(context);

    PaymentRuleContext context2 =
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("VISA")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount("fixme")
            .paymentCurrency("SEK")
            .build();
    ruleSetExecutionService.evaluateRules(context2);

    PaymentRuleContext context3 =
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("VISA")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount("fixme")
            .paymentCurrency("EUR")
            .build();
    ruleSetExecutionService.evaluateRules(context3);
  }
}
