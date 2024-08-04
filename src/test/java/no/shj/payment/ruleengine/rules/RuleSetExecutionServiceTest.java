package no.shj.payment.ruleengine.rules;

import com.neovisionaries.i18n.CountryCode;
import java.math.BigDecimal;
import no.shj.payment.ruleengine.database.RuleConfigurationDao;
import no.shj.payment.ruleengine.ruleservice.RuleSetExecutionService;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class RuleSetExecutionServiceTest {

  private RuleSetExecutionService ruleSetExecutionService;

  // Mocking away the Cosmos DB - could also use test containers but had some issues with the setup.
  @MockBean private RuleConfigurationDao ruleConfigurationDao;

  @Test
  @Disabled
  void evaluateRules() {

    PaymentRuleContext context =
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("MASTERCARD")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount(BigDecimal.TEN)
            .paymentCurrency("SEK")
            .cardType("DEBIT")
            .build();

    ruleSetExecutionService.evaluateRules(context);

    PaymentRuleContext context2 =
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("VISA")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount(BigDecimal.TEN)
            .paymentCurrency("SEK")
            .cardType("CREDIT")
            .build();
    ruleSetExecutionService.evaluateRules(context2);

    PaymentRuleContext context3 =
        PaymentRuleContext.PaymentRuleContextBuilder.builder()
            .customerType("EMPLOYEE")
            .paymentMethod("VISA")
            .paymentOriginCountry(CountryCode.valueOf("NO"))
            .transactionAmount(BigDecimal.valueOf(3000))
            .paymentCurrency("EUR")
            .cardType("CREDIT")
            .build();
    ruleSetExecutionService.evaluateRules(context3);
  }
}
