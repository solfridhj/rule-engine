package no.shj.payment.ruleengine.function.ruleconfig.response;

public class ExistingRulesResponseDto {

  private String id;
  private String ruleId;
  private String ruleDescription;
  private Boolean isActive;
  private String structureVersion;
  private String ruleSpecificConfigurationVersion;
  private Object ruleSpecificConfigurationData;

  public String getId() {
    return id;
  }

  public ExistingRulesResponseDto setId(String id) {
    this.id = id;
    return this;
  }

  public String getRuleId() {
    return ruleId;
  }

  public ExistingRulesResponseDto setRuleId(String ruleId) {
    this.ruleId = ruleId;
    return this;
  }

  public String getRuleDescription() {
    return ruleDescription;
  }

  public ExistingRulesResponseDto setRuleDescription(String ruleDescription) {
    this.ruleDescription = ruleDescription;
    return this;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public ExistingRulesResponseDto setIsActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public String getStructureVersion() {
    return structureVersion;
  }

  public ExistingRulesResponseDto setStructureVersion(String structureVersion) {
    this.structureVersion = structureVersion;
    return this;
  }

  public String getRuleSpecificConfigurationVersion() {
    return ruleSpecificConfigurationVersion;
  }

  public ExistingRulesResponseDto setRuleSpecificConfigurationVersion(
      String ruleSpecificConfigurationVersion) {
    this.ruleSpecificConfigurationVersion = ruleSpecificConfigurationVersion;
    return this;
  }

  public Object getRuleSpecificConfigurationData() {
    return ruleSpecificConfigurationData;
  }

  public ExistingRulesResponseDto setRuleSpecificConfigurationData(
      Object ruleSpecificConfigurationData) {
    this.ruleSpecificConfigurationData = ruleSpecificConfigurationData;
    return this;
  }
}
