package no.shj.payment.ruleengine.database;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RuleConfigurationDao {

  Optional<@Valid RuleConfigurationEntity> getRuleConfigurationEntity(
      @NotNull Rule rule, @NotNull Integer ruleVersion);

  Optional<@Valid RuleConfigurationEntity> getRuleConfigurationEntityNotCached(
      @NotNull Rule rule, @NotNull Integer ruleVersion);

  @Valid
  RuleConfigurationEntity saveRuleConfiguration(@Valid RuleConfigurationEntity ruleConfiguration);
}
