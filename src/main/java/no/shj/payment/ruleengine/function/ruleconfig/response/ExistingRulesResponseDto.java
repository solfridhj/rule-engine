package no.shj.payment.ruleengine.function.ruleconfig.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ExistingRulesResponseDto {

  private String id;
  @NotBlank private String ruleId;
  private String createdDate;
  private String createdBy;
  private Boolean isActive;
  private String structureVersion;
  private String ruleSpecificConfigurationVersion;
  private Object ruleSpecificConfigurationData;

  // Not use lombok for these
  public Boolean getIsActive() {
    return isActive;
  }

  public ExistingRulesResponseDto setIsActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }
}
