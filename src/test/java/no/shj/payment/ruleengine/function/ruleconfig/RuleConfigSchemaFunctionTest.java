package no.shj.payment.ruleengine.function.ruleconfig;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RuleConfigSchemaFunctionTest {

    RuleConfigSchemaFunction function = new RuleConfigSchemaFunction();

    @Test
    void getSchemaForAllRules() {
        var outputStringList = function.apply(null);
        assertThat(outputStringList).hasSize(3);
    }
}