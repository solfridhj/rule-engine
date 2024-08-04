package no.shj.payment.ruleengine.function.ruleconfig;

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
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

@Component
public class RuleConfigSchemaFunction implements Function<Void, List<JsonNode>> {

  @Override
  public List<JsonNode> apply(Void notUsed) {

    List<JsonNode> printedRules = new ArrayList<>();

    Reflections reflections = new Reflections("no.shj.payment.ruleengine");
    Set<Class<?>> allRules = reflections.getTypesAnnotatedWith(RuleMetadata.class);
    for (Class<?> clazz : allRules) {
      ParameterizedType asParametrizedType = (ParameterizedType) clazz.getGenericSuperclass();
      Type[] typeArguments = asParametrizedType.getActualTypeArguments();
      Type configType = typeArguments[1];

      JakartaValidationModule module =
          new JakartaValidationModule(JakartaValidationOption.INCLUDE_PATTERN_EXPRESSIONS);
      SchemaGeneratorConfigBuilder configBuilder =
          new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON)
              .with(module);
      SchemaGeneratorConfig config = configBuilder.build();
      SchemaGenerator generator = new SchemaGenerator(config);
      JsonNode jsonSchema = generator.generateSchema(configType);
      printedRules.add(jsonSchema);
    }
    return printedRules;
  }
}
