package no.shj.payment.ruleengine.rules;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.ACQUIRER_ROUTING;
import static no.shj.payment.ruleengine.ruleservice.rules.Rule.PAYMENT_METHOD_FROM_COUNTRY;
import static no.shj.payment.ruleengine.testconfig.ReflectionsTestsConfig.createTestReflections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.neovisionaries.i18n.CountryCode;
import java.math.BigDecimal;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.mocktestrule.MockTest1Rule;
import no.shj.payment.ruleengine.ruleservice.RuleSetExecutionService;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RuleSetExecutionServiceTest {

  @Autowired private ApplicationContext applicationContext;
  private RuleSetExecutionService ruleSetExecutionService;

  // Mocking away the Cosmos DB - could also use test containers but had some issues with the setup.
  @MockBean private RuleConfigurationDaoImpl ruleConfigurationDao;

  @BeforeEach
  public void setUp() {
    // Create the Reflections instance for the test classpath
    Reflections reflections = createTestReflections();
    // Instantiate the RuleSetExecutionService with the injected ApplicationContext and the created
    // Reflections instance
    ruleSetExecutionService = new RuleSetExecutionService(applicationContext, reflections);
  }

  @Test
  void evaluateRules_When2TestRules_ThenExecutesBothRules() {
    when(ruleConfigurationDao.getRuleConfigurationEntity(ACQUIRER_ROUTING, 1))
        .thenReturn(
            Optional.of(
                new RuleConfigurationEntity()
                    .setRuleId(ACQUIRER_ROUTING)
                    .setIsActive(true)
                    .setRuleSpecificConfigurationVersion(1)
                    .setRuleSpecificConfigurationData(
                        new MockTest1Rule.MockConfig().setMockObject("rule1Data"))));

    when(ruleConfigurationDao.getRuleConfigurationEntity(PAYMENT_METHOD_FROM_COUNTRY, 1))
        .thenReturn(
            Optional.of(
                new RuleConfigurationEntity()
                    .setRuleId(PAYMENT_METHOD_FROM_COUNTRY)
                    .setIsActive(true)
                    .setRuleSpecificConfigurationVersion(1)
                    .setRuleSpecificConfigurationData(
                        new MockTest1Rule.MockConfig().setMockObject("rule2Data"))));

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

    // Validate that context has been updated to verify that both rules were identified and
    // executed.
    var ruleExecutionResult = context.getRuleExecutionResult();
    assertThat(ruleExecutionResult.getAcquirerId()).isEqualTo("Mock1");
    assertThat(ruleExecutionResult.getPaymentMethod()).isEqualTo("Mock2");
  }
}
