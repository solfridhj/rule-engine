package no.shj.payment.ruleengine.ruleservice;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

@Service
public class ExistingRulesService {

  private final Reflections reflections;

  public ExistingRulesService(Reflections reflections) {
    this.reflections = reflections;
  }

  public Set<RuleAndVersion> getAllExistingRules() {
    var existingRules = reflections.getTypesAnnotatedWith(RuleMetadata.class);
    return existingRules.stream()
        .map(clazz -> clazz.getAnnotation(RuleMetadata.class))
        .map(metadata -> new RuleAndVersion(metadata.ruleId(), metadata.version()))
        .collect(Collectors.toSet());
  }

  @Data
  @AllArgsConstructor
  @Accessors(chain = true)
  public static class RuleAndVersion {
    private Rule ruleId;
    private Integer version;
  }
}
