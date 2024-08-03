package no.shj.payment.ruleengine.ruleservice;

import java.util.Set;
import java.util.stream.Collectors;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

@Service
public class ExistingRulesService {

  public Set<Rule> getAllExistingRules() {
    Reflections reflections = new Reflections("no.shj.payment.ruleengine");
    var existingRules = reflections.getTypesAnnotatedWith(RuleMetadata.class);
    return existingRules.stream()
        .map(clazz -> clazz.getAnnotation(RuleMetadata.class))
        .map(RuleMetadata::ruleId)
        .collect(Collectors.toSet());
  }
}
