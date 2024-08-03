package no.shj.payment.ruleengine.database;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RuleConfigurationDao<T> {

  Optional<@NotNull @Valid RuleConfigurationEntity<T>> getRuleConfigurationEntity(
      @NotNull Rule rule);
}
