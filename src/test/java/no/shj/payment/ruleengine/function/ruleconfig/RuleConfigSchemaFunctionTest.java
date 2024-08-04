package no.shj.payment.ruleengine.function.ruleconfig;

import static no.shj.payment.ruleengine.testconfig.ReflectionsTestsConfig.createTestReflections;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class RuleConfigSchemaFunctionTest {

  private final Reflections reflections = createTestReflections();
  RuleConfigSchemaFunction function = new RuleConfigSchemaFunction(reflections);

  @Test
  void getSchemaForAllRules() {
    var outputList = function.apply(null);
    assertThat(outputList).hasSize(2);
    // assertThat(outputList.getFirst().getJsonNode()).contains("mockObject");
    // assertThat(outputList.getFirst().getRuleId()).isEqualTo(ACQUIRER_ROUTING);
  }
}
