package no.shj.payment.ruleengine.database;

import java.util.Optional;
import no.shj.payment.ruleengine.database.implementation.RuleConfigurationRepository;
import no.shj.payment.ruleengine.service.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class RuleConfigurationDaoImpl<T> implements RuleConfigurationDao<T> {

  Logger log = LoggerFactory.getLogger(this.getClass());

  private final RuleConfigurationRepository<T> repository;

  public RuleConfigurationDaoImpl(RuleConfigurationRepository<T> repository) {
    this.repository = repository;
  }

  @Cacheable("rule-config")
  public Optional<RuleConfigurationEntity<T>> getRuleConfigurationEntity(Rule rule) {
    log.debug("Not using cache for rule {}", rule);
    return repository.findByRuleId(rule);
  }
}
