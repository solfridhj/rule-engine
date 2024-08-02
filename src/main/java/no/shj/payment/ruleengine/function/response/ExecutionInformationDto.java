package no.shj.payment.ruleengine.function.response;

import java.util.Map;

public class ExecutionInformationDto {

  String ruleId;
  String ruleDescription;
  Map<String, Object> ruleInput;
  boolean wasTriggered;

  public static final class ExecutionInformationBuilder {
    private String ruleId;
    private String ruleDescription;
    private Map<String, Object> ruleInput;
    private boolean wasTriggered;

    private ExecutionInformationBuilder() {}

    public static ExecutionInformationBuilder builder() {
      return new ExecutionInformationBuilder();
    }

    public ExecutionInformationBuilder ruleId(String ruleId) {
      this.ruleId = ruleId;
      return this;
    }

    public ExecutionInformationBuilder ruleDescription(String ruleDescription) {
      this.ruleDescription = ruleDescription;
      return this;
    }

    public ExecutionInformationBuilder ruleInput(Map<String, Object> ruleInput) {
      this.ruleInput = ruleInput;
      return this;
    }

    public ExecutionInformationBuilder wasTriggered(boolean wasTriggered) {
      this.wasTriggered = wasTriggered;
      return this;
    }

    public ExecutionInformationDto build() {
      ExecutionInformationDto executionInformationDto = new ExecutionInformationDto();
      executionInformationDto.ruleInput = this.ruleInput;
      executionInformationDto.wasTriggered = this.wasTriggered;
      executionInformationDto.ruleId = this.ruleId;
      executionInformationDto.ruleDescription = this.ruleDescription;
      return executionInformationDto;
    }
  }
}
