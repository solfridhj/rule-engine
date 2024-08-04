package no.shj.payment.ruleengine.function.ruleconfig.updaterule;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateRuleConfigurationResponseDto {

  @NotNull private Rule ruleId;
  @NotNull private Boolean isActive;

  @NotNull private String createdDate;
  @NotNull private String createdBy;
  // Rule specific configuration data
  @NotNull private Object ruleSpecificConfigurationData;


  // Have to define these and not use lombok
  public @NotNull Boolean getIsActive() {
    return isActive;
  }

  public UpdateRuleConfigurationResponseDto setIsActive(@NotNull Boolean active) {
    isActive = active;
    return this;
  }
}
