package no.shj.payment.ruleengine.configservice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import no.shj.payment.ruleengine.database.RuleConfigurationDao;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.ruleservice.ExistingRulesService;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.reflections.Reflections;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;

@Service
public class RuleConfigService {

  private final RuleConfigurationDao ruleConfigurationDao;
  private final ExistingRulesService existingRulesService;
  private final Reflections reflections;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public RuleConfigService(
      RuleConfigurationDao ruleConfigurationDao,
      ExistingRulesService existingRulesService,
      Reflections reflections) {
    this.ruleConfigurationDao = ruleConfigurationDao;
    this.existingRulesService = existingRulesService;
    this.reflections = reflections;
  }

  public Set<RuleConfigurationEntity> getAllRulesAndConfig() {
    var ruleIdAndVersions = existingRulesService.getAllExistingRules();

    Set<RuleConfigurationEntity> allRuleConfigurations = new HashSet<>();

    for (var ruleIdAndVersion : ruleIdAndVersions) {
      var storedConfiguration =
          ruleConfigurationDao.getRuleConfigurationEntityNotCached(
              ruleIdAndVersion.getRuleId(), ruleIdAndVersion.getVersion());
      if (storedConfiguration.isEmpty()) {
        // No configuration has been added yet - won't be possible to execute the rule.
        allRuleConfigurations.add(
            new RuleConfigurationEntity().setRuleId(ruleIdAndVersion.getRuleId()));
      } else {
        // Note: Not mapping the rule specific configuration to the object type as the object will
        // be corret
        // in the request anyway.
        allRuleConfigurations.add(storedConfiguration.get());
      }
    }
    return allRuleConfigurations;
  }

  public RuleConfigurationEntity saveRuleConfiguration(RuleConfigurationEntity entity) {
    Integer ruleVersionNr = null;
    boolean foundClass = false;
    Set<Class<?>> allRules = reflections.getTypesAnnotatedWith(RuleMetadata.class);

    Rule ruleId = entity.getRuleId();
    for (Class<?> clazz : allRules) {
      RuleMetadata annotation = clazz.getAnnotation(RuleMetadata.class);
      if (annotation != null && ruleId.equals(annotation.ruleId())) {
        foundClass = true;
        ruleVersionNr = annotation.version();
        Object object = mapToRuleSpecificConfigDataType(entity, clazz);
        entity.setRuleSpecificConfigurationData(object);
      }
    }

    if (!foundClass) {
      ProblemDetail problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
      problemDetail.setTitle("Bad Request");
      problemDetail.setDetail(
          String.format("Invalid request. Rule with id %s does not exist.", ruleId));
      throw new RuleConfigException(problemDetail);
    }

    entity.setId(String.valueOf(UUID.randomUUID()));
    entity.setCreatedBy("solfrid.hagen.johansen@outlook.com"); // TODO - get from header
    entity.setCreatedDate(LocalDate.now());
    entity.setStructureVersion(1);
    entity.setRuleSpecificConfigurationVersion(ruleVersionNr);

    return ruleConfigurationDao.saveRuleConfiguration(entity);
  }

  /**
   * Helper method to map to the rule specific config data type.
   *
   * <ol>
   *   Map class to parametrized type
   * </ol>
   *
   * <ol>
   *   Get a list of the actual type arguments. This is a list of the two generics which are
   *   required to pass as an argument to the abstract rule {@link
   *   no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule}.
   * </ol>
   *
   * <ol>
   *   Get the second element, as that is how we will find the type of the required configuration.
   * </ol>
   *
   * @param entity input entity with rule specific config not of the correct type
   * @param clazz class of the identified rule the entity will create a new configuration for
   * @return rule specific config as the correct object type. Throws an exception if not possible.
   */
  private Object mapToRuleSpecificConfigDataType(RuleConfigurationEntity entity, Class<?> clazz) {
    try {
      ParameterizedType asParametrizedType = (ParameterizedType) clazz.getGenericSuperclass();
      Type[] typeArguments = asParametrizedType.getActualTypeArguments();
      Type configType = typeArguments[1];

      TypeReference<?> typeReference =
          new TypeReference<>() {
            @Override
            public Type getType() {
              return configType;
            }
          };

      return objectMapper.convertValue(entity.getRuleSpecificConfigurationData(), typeReference);

    } catch (Exception e) {
      ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
      problemDetail.setTitle("Bad request");
      problemDetail.setDetail("Incorrect format of rule specific configuration");
      throw new RuleConfigException(problemDetail);
    }
  }
}
