package no.shj.payment.ruleengine.ruleservice;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

class ExistingRulesServiceTest {

  private final Reflections reflections = new Reflections("no.shj.payment.ruleengine");
  private final ExistingRulesService existingRulesService = new ExistingRulesService(reflections);

  @Test
  void getAllExistingRules() {
    var result = existingRulesService.getAllExistingRules();

    assertThat(result)
        .hasSize(4)
        .contains(
            new ExistingRulesService.RuleAndVersion(PAYMENT_METHOD_FROM_COUNTRY, 1),
            new ExistingRulesService.RuleAndVersion(ACQUIRER_ROUTING, 1),
            new ExistingRulesService.RuleAndVersion(FEE_CALCULATION, 1),
            new ExistingRulesService.RuleAndVersion(MANUAL_HANDLING_DUE_TO_AMOUNT_EVALUATION, 1));
  }
}
