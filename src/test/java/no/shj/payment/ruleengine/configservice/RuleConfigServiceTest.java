package no.shj.payment.ruleengine.configservice;

import static no.shj.payment.ruleengine.ruleservice.rules.Rule.PAYMENT_METHOD_FROM_COUNTRY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import no.shj.payment.ruleengine.database.RuleConfigurationDao;
import no.shj.payment.ruleengine.database.RuleConfigurationEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RuleConfigServiceTest {

  @Autowired private RuleConfigService service;

  @MockBean private RuleConfigurationDao ruleConfigurationDao;

  @Test
  @Disabled
  void getAllRulesAndConfig() {
    var result = service.getAllRulesAndConfig();
  }

  @Test
  @Disabled
  void updateRuleConfig() {

    Object ruleSpecificConfig =
        """
            "paymentMethodMappings": [
                        {
                            "paymentOriginCountryCode": "NO",
                            "paymentMethod": "VIPPS"
                        },
                        {
                            "paymentOriginCountryCode": "SE",
                            "paymentMethod": "SWISH"
                        },
                        {
                            "paymentOriginCountryCode": "US",
                            "paymentMethod": "CHECK"
                        },
                        {
                            "paymentOriginCountryCode": "UK",
                            "paymentMethod": "WIRE TRANSFER"
                        }
                    ]
            """;

    var ruleConfig =
        new RuleConfigurationEntity()
            .setRuleId(PAYMENT_METHOD_FROM_COUNTRY)
            .setIsActive(true)
            .setStructureVersion(1)
            .setRuleSpecificConfigurationVersion(1)
            .setCreatedDate(LocalDate.of(2024, 8, 3))
            .setCreatedBy("solfrid.hagen.johansen@outlook.com")
            .setRuleSpecificConfigurationData(ruleSpecificConfig);

    var result = service.saveRuleConfiguration(ruleConfig);
  }
}
