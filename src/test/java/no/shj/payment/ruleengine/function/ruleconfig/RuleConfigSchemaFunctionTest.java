package no.shj.payment.ruleengine.function.ruleconfig;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RuleConfigSchemaFunctionTest {

  RuleConfigSchemaFunction function = new RuleConfigSchemaFunction();

  @Test
  void getSchemaForAllRules() {
    var outputStringList = function.apply(null);
    assertThat(outputStringList).hasSize(3);
  }
}
