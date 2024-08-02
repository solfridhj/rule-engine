package no.shj.payment.ruleengine.database;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import java.util.Optional;
import no.shj.payment.ruleengine.rules.Rule;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleConfigurationRepository<T>
    extends CosmosRepository<RuleConfigurationEntity<T>, String> {

  // todo - caching?
  Optional<RuleConfigurationEntity<T>> findByRuleId(Rule ruleId);
}
