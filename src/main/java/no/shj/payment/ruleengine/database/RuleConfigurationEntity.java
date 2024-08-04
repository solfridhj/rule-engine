package no.shj.payment.ruleengine.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;
import org.springframework.data.annotation.Id;

@Data
@Accessors(chain = true)
@Document(collection = "rule-configuration-container")
@NoArgsConstructor
public class RuleConfigurationEntity {

  @Id @NotBlank private String id;

  @NotNull private Rule ruleId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate createdDate;

  @NotNull private String createdBy;
  @NotNull private Integer structureVersion;

  // General configuration data - common for all rules
  private boolean isActive;

  // Rule specific configuration data
  @NotNull private Integer ruleSpecificConfigurationVersion;
  @NotNull @Valid private Object ruleSpecificConfigurationData;

  // Need to override the lombok generated setters and getters for isActive as it's a boolean.
  public boolean getIsActive() {
    return isActive;
  }

  public RuleConfigurationEntity setIsActive(boolean isActive) {
    this.isActive = isActive;
    return this;
  }
}
