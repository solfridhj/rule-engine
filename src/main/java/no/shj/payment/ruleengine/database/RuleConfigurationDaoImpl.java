package no.shj.payment.ruleengine.database;

import java.util.Optional;
import no.shj.payment.ruleengine.database.implementation.RuleConfigurationRepository;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Repository
@Validated
public class RuleConfigurationDaoImpl implements RuleConfigurationDao {

  Logger log = LoggerFactory.getLogger(this.getClass());

  private final RuleConfigurationRepository repository;

  public RuleConfigurationDaoImpl(RuleConfigurationRepository repository) {
    this.repository = repository;
  }

  @Cacheable("rule-config")
  public Optional<RuleConfigurationEntity> getRuleConfigurationEntity(
      Rule rule, Integer ruleVersion) {
    log.debug("Not using cache for rule {}", rule);
    return repository.findByRuleIdAndLastCreatedAndCorrectRuleVersion(rule, ruleVersion).stream()
        .findFirst();
  }

  // Need to have a non-cached version for viewing the most recent configuration.
  public Optional<RuleConfigurationEntity> getRuleConfigurationEntityNotCached(
      Rule rule, Integer ruleVersion) {
    return repository.findByRuleIdAndLastCreatedAndCorrectRuleVersion(rule, ruleVersion).stream()
        .findFirst();
  }

  public RuleConfigurationEntity saveRuleConfiguration(RuleConfigurationEntity ruleConfiguration) {
    log.trace("Saving new rule configuration for rule {}", ruleConfiguration.getRuleId());
    return repository.save(ruleConfiguration);
  }
}
