package no.shj.payment.ruleengine.configservice;

import java.util.ArrayList;
import java.util.List;
import no.shj.payment.ruleengine.database.RuleConfigurationDao;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.ruleservice.ExistingRulesService;
import org.springframework.stereotype.Service;

@Service
public class RuleConfigService {

  private final RuleConfigurationDao<Object> ruleConfigurationDao;
  private final ExistingRulesService existingRulesService;

  public RuleConfigService(
      RuleConfigurationDao<Object> ruleConfigurationDao,
      ExistingRulesService existingRulesService) {
    this.ruleConfigurationDao = ruleConfigurationDao;
    this.existingRulesService = existingRulesService;
  }

  public List<RuleConfigurationEntity<Object>> getAllRulesAndConfig() {
    var ruleIds = existingRulesService.getAllExistingRules();

    ArrayList<RuleConfigurationEntity<Object>> allRuleConfigurations = new ArrayList<>();

    for (var ruleId : ruleIds) {
      var storedConfiguration = ruleConfigurationDao.getRuleConfigurationEntity(ruleId);
      if (storedConfiguration.isEmpty()) {
        allRuleConfigurations.add(new RuleConfigurationEntity<>().setRuleId(ruleId));
      } else {
        allRuleConfigurations.add(storedConfiguration.get());
      }
    }
    return allRuleConfigurations;
  }
}
