package no.shj.payment.ruleengine.ruleservice.genericengine;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.ACQUIRER_ROUTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.mocktestrule.MockTest1Rule;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractRuleTest {

  @Mock private RuleConfigurationDaoImpl dao;

  @InjectMocks private MockTest1Rule mockTest1Rule;

  @Test
  void execute_WhenRuleWithConfigAndActive_ThenExecute() {

    // The data from the dao contains "Object" for the rule specific configuration data, which will
    // be saved as a map realistically. So testing that mapping from Map -> data type of rule config
    // works.
    var mockMap = Map.of("mockObject", "ruleData1");

    when(dao.getRuleConfigurationEntity(ACQUIRER_ROUTING, 1))
        .thenReturn(
            Optional.of(
                new RuleConfigurationEntity()
                    .setRuleId(ACQUIRER_ROUTING)
                    .setIsActive(true)
                    .setRuleSpecificConfigurationVersion(1)
                    .setRuleSpecificConfigurationData(mockMap)));
    PaymentRuleContext context =
        PaymentRuleContext.PaymentRuleContextBuilder.builder().cardType("DEBIT").build();
    mockTest1Rule.executeRule(context);

    // Validate that the context has been updated with execution info
    var ruleExecutionInfo = context.getRuleExecutionInformationList().getFirst();
    assertThat(ruleExecutionInfo.getRuleId()).isEqualTo(ACQUIRER_ROUTING);
    assertThat(ruleExecutionInfo.getRuleDescription()).isEqualTo("Mock rule for testing only");
    assertThat(ruleExecutionInfo.isWasTriggered()).isTrue();
    assertThat(ruleExecutionInfo.getRuleInput()).containsEntry("mockValue1", "DEBIT");
    // Verify that the output has been updated
    assertThat(context.getRuleExecutionResult().getAcquirerId()).isEqualTo("Mock1");
  }

  @Test
  void execute_WhenRuleWithConfigAndNotActive_ThenDoNotExecute() {

    when(dao.getRuleConfigurationEntity(ACQUIRER_ROUTING, 1))
        .thenReturn(
            Optional.of(
                new RuleConfigurationEntity()
                    .setRuleId(ACQUIRER_ROUTING)
                    .setIsActive(false)
                    .setRuleSpecificConfigurationVersion(1)
                    .setRuleSpecificConfigurationData(
                        new MockTest1Rule.MockConfig().setMockObject("rule1Data"))));
    PaymentRuleContext context =
        PaymentRuleContext.PaymentRuleContextBuilder.builder().cardType("DEBIT").build();
    mockTest1Rule.executeRule(context);

    assertThat(context.getRuleExecutionInformationList()).isEmpty();
    // Verify that the output has NOT been updated
    assertThat(context.getRuleExecutionResult().getAcquirerId()).isNull();
  }

  @Test
  void execute_WhenRuleWithoutConfig_ThenDoNotExecute() {

    when(dao.getRuleConfigurationEntity(ACQUIRER_ROUTING, 1)).thenReturn(Optional.empty());
    PaymentRuleContext context =
        PaymentRuleContext.PaymentRuleContextBuilder.builder().cardType("DEBIT").build();
    mockTest1Rule.executeRule(context);

    assertThat(context.getRuleExecutionInformationList()).isEmpty();
    // Verify that the output has NOT been updated
    assertThat(context.getRuleExecutionResult().getAcquirerId()).isNull();
  }
}
