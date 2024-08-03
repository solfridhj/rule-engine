package no.shj.payment.ruleengine.database;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import no.shj.payment.ruleengine.service.rules.Rule;
import org.springframework.data.annotation.Id;

// TODO - keep it hexagonal, should map this to domain-objects to separate from the port.
@Document(collection = "rule-configuration-container")
public class RuleConfigurationEntity<T> {

  @Id @NotBlank private String id;
  @NotNull private Rule ruleId;
  @Nullable private LocalDateTime createdTimeStamp;
  @Nullable private LocalDateTime updatedTimeStamp;

  // General configuration data - common for all rules
  private boolean isActive;

  // Rule specific configuration data
  @NotNull private T ruleSpecificConfigurationData;

  public String getId() {
    return id;
  }

  public RuleConfigurationEntity<T> setId(String id) {
    this.id = id;
    return this;
  }

  public Rule getRuleId() {
    return ruleId;
  }

  public RuleConfigurationEntity<T> setRuleId(Rule ruleId) {
    this.ruleId = ruleId;
    return this;
  }

  public LocalDateTime getCreatedTimeStamp() {
    return createdTimeStamp;
  }

  public RuleConfigurationEntity<T> setCreatedTimeStamp(LocalDateTime createdTimeStamp) {
    this.createdTimeStamp = createdTimeStamp;
    return this;
  }

  public LocalDateTime getUpdatedTimeStamp() {
    return updatedTimeStamp;
  }

  public RuleConfigurationEntity<T> setUpdatedTimeStamp(LocalDateTime updatedTimeStamp) {
    this.updatedTimeStamp = updatedTimeStamp;
    return this;
  }

  public boolean isActive() {
    return isActive;
  }

  public RuleConfigurationEntity<T> setIsActive(boolean active) {
    isActive = active;
    return this;
  }

  public T getRuleSpecificConfigurationData() {
    return ruleSpecificConfigurationData;
  }

  public RuleConfigurationEntity<T> setRuleSpecificConfigurationData(
      T ruleSpecificConfigurationData) {
    this.ruleSpecificConfigurationData = ruleSpecificConfigurationData;
    return this;
  }
}
