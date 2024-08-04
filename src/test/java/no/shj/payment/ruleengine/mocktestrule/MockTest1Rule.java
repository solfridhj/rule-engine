package no.shj.payment.ruleengine.mocktestrule;

import java.util.Map;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionResult;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;

/**
 * Mock rule creating for testing purposes, to ensure reflection and other setup is working as
 * expected.
 */
@RuleMetadata(
    ruleId = Rule.ACQUIRER_ROUTING,
    version = 1,
    ruleDescription = "Mock rule for testing only")
public class MockTest1Rule extends AbstractRule<String, MockTest1Rule.MockConfig> {

  public MockTest1Rule(RuleConfigurationDaoImpl ruleConfigurationDao) {
    super(ruleConfigurationDao, MockTest1Rule.MockConfig.class);
  }

  @Override
  protected Map<String, Object> ruleInput(PaymentRuleContext context) {
    return Map.of("mockValue1", context.getCardType());
  }

  @Override
  protected Optional<String> ruleLogic(PaymentRuleContext context, MockConfig ruleSpecificConfig) {
    return Optional.of("Mock1");
  }

  @Override
  protected void setResult(RuleExecutionResult result, String output) {
    result.setAcquirerId(output);
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class MockConfig {
    private String mockObject;
  }
}
