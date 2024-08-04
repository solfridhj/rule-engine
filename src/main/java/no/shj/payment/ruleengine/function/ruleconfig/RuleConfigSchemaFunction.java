package no.shj.payment.ruleengine.function.ruleconfig;

import static com.github.victools.jsonschema.generator.Option.EXTRA_OPEN_API_FORMAT_VALUES;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationOption;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

@Component
public class RuleConfigSchemaFunction
    implements Function<Void, List<RuleConfigSchemaFunction.SchemaInformation>> {

  private final Reflections reflections;

  public RuleConfigSchemaFunction(Reflections reflections) {
    this.reflections = reflections;
  }

  @Override
  public List<SchemaInformation> apply(Void notUsed) {

    List<SchemaInformation> printedRules = new ArrayList<>();

    Set<Class<?>> allRules = reflections.getTypesAnnotatedWith(RuleMetadata.class);
    for (Class<?> clazz : allRules) {

      var metadata = clazz.getAnnotation(RuleMetadata.class);
      ParameterizedType asParametrizedType = (ParameterizedType) clazz.getGenericSuperclass();
      Type[] typeArguments = asParametrizedType.getActualTypeArguments();
      Type configType = typeArguments[1];

      JakartaValidationModule module =
          new JakartaValidationModule(JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS);

      SchemaGeneratorConfigBuilder configBuilder =
          new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7, OptionPreset.PLAIN_JSON)
              .with(module)
              .with(EXTRA_OPEN_API_FORMAT_VALUES);
      SchemaGeneratorConfig config = configBuilder.build();
      SchemaGenerator generator = new SchemaGenerator(config);
      JsonNode jsonSchema = generator.generateSchema(configType);

      printedRules.add(
          new SchemaInformation().setJsonNode(jsonSchema).setRuleId(metadata.ruleId()));
    }
    return printedRules;
  }

  @Data
  @NoArgsConstructor
  @Accessors(chain = true)
  public static class SchemaInformation {
    private Rule ruleId;
    private JsonNode jsonNode;
  }
}
