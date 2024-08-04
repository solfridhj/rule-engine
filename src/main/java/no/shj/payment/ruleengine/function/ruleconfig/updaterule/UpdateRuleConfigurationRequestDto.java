package no.shj.payment.ruleengine.function.ruleconfig.updaterule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UpdateRuleConfigurationRequestDto {

  @NotNull private Rule ruleId;
  @NotNull private Boolean isActive;
  // Rule specific configuration data
  @NotNull private Object ruleSpecificConfigurationData;

  // Can't use lombok for these.
  public @NotNull Boolean getIsActive() {
    return isActive;
  }

  public UpdateRuleConfigurationRequestDto setIsActive(@NotNull Boolean active) {
    isActive = active;
    return this;
  }
}
