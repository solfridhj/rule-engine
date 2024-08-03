package no.shj.payment.ruleengine.database.implementation;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.service.rules.Rule;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleConfigurationRepository<T>
    extends CosmosRepository<RuleConfigurationEntity<T>, String> {

  Optional<RuleConfigurationEntity<T>> findByRuleId(Rule ruleId);
}
