package no.shj.payment.ruleengine.database.implementation;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import java.util.ArrayList;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleConfigurationRepository
    extends CosmosRepository<RuleConfigurationEntity, String> {

  @Query(
      "SELECT * FROM c WHERE c.ruleId = @ruleId AND c.ruleSpecificConfigurationVersion = @ruleVersion ORDER BY c._ts DESC OFFSET 0 LIMIT 1")
  ArrayList<RuleConfigurationEntity> findByRuleIdAndLastCreatedAndCorrectRuleVersion(
      @Param("ruleId") Rule ruleId, @Param("ruleVersion") Integer ruleVersion);
}
