package no.shj.payment.ruleengine.configservice;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.ACQUIRER_ROUTING;
import static no.shj.payment.ruleengine.ruleservice.rules.Rule.PAYMENT_METHOD_FROM_COUNTRY;
import static no.shj.payment.ruleengine.testconfig.ReflectionsTestsConfig.createTestReflections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;
import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import no.shj.payment.ruleengine.mocktestrule.MockTest1Rule;
import no.shj.payment.ruleengine.ruleservice.ExistingRulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RuleConfigServiceTest {

  // Need to mock the dao layer away due to lack of option for emulation on Mac - would normally
  // have this as a complete
  // integration test.
  @MockBean private RuleConfigurationDaoImpl ruleConfigurationDao;

  private RuleConfigService service;

  @BeforeEach
  void setup() {
    Reflections reflections = createTestReflections();
    ExistingRulesService existingRulesService = new ExistingRulesService(reflections);
    service = new RuleConfigService(ruleConfigurationDao, existingRulesService, reflections);
  }

  @Test
  void getAllRulesAndConfig() {

    // The data from the dao contains "Object" for the rule specific configuration data, which will
    // be saved as a map realistically. So testing that mapping from Map -> data type of rule config
    // works.
    var mockMap = Map.of("mockObject", "ruleData1");

    when(ruleConfigurationDao.getRuleConfigurationEntityNotCached(ACQUIRER_ROUTING, 1))
        .thenReturn(
            Optional.of(
                new RuleConfigurationEntity()
                    .setId("2323")
                    .setRuleId(ACQUIRER_ROUTING)
                    .setIsActive(true)
                    .setRuleSpecificConfigurationVersion(1)
                    .setRuleSpecificConfigurationData(mockMap)));

    // Intentionally not mocking dao for rule 2 to test scenario where config has not yet been
    // created.

    var result = service.getAllRulesAndConfig();
    var expectedRule1 =
        new RuleConfigurationEntity()
            .setId("2323")
            .setRuleId(ACQUIRER_ROUTING)
            .setIsActive(true)
            .setRuleSpecificConfigurationVersion(1)
            .setRuleSpecificConfigurationData(Map.of("mockObject", "ruleData1"));
    var expectedRule2 = new RuleConfigurationEntity().setRuleId(PAYMENT_METHOD_FROM_COUNTRY);
    assertThat(result).contains(expectedRule1, expectedRule2);
  }

  @Test
  void updateRuleConfig() {

    var ruleConfig =
        new RuleConfigurationEntity()
            .setRuleId(ACQUIRER_ROUTING)
            .setIsActive(true)
            .setRuleSpecificConfigurationVersion(1)
            .setRuleSpecificConfigurationData(Map.of("mockObject", "someUpdatedValue"));

    when(ruleConfigurationDao.saveRuleConfiguration(any())).thenReturn(ruleConfig);
    var result = service.saveRuleConfiguration(ruleConfig);

    assertThat(result.getId()).isNotNull();
    assertThat(result.getCreatedDate()).isNotNull();
    assertThat(result.getCreatedBy()).isEqualTo("solfrid.hagen.johansen@outlook.com");
    assertThat(result.getStructureVersion()).isEqualTo(1);
    assertThat(result.getRuleSpecificConfigurationData())
        .isInstanceOf(MockTest1Rule.MockConfig.class);
    var asRuleConfigClass = (MockTest1Rule.MockConfig) result.getRuleSpecificConfigurationData();
    assertThat(asRuleConfigClass.getMockObject()).isEqualTo("someUpdatedValue");
  }

  @Test
  void updateRuleConfig_WhenInvalidRuleSpecConfigData_ThenThrowException() {

    var ruleConfig =
        new RuleConfigurationEntity()
            .setRuleId(ACQUIRER_ROUTING)
            .setIsActive(true)
            .setRuleSpecificConfigurationVersion(1)
            .setRuleSpecificConfigurationData(Map.of("mockObject", Map.of("currency", "SEK")));

    assertThatThrownBy(() -> service.saveRuleConfiguration(ruleConfig))
        .isInstanceOf(RuleConfigException.class);
  }
}
