package no.shj.payment.ruleengine.context;

import java.util.HashMap;
import java.util.Map;

public class RuleExecutionInformation {

  String ruleId;
  String ruleDescription;
  Map<String, Object> ruleInput = new HashMap<>();
  boolean wasTriggered = false;

  public String getRuleId() {
    return ruleId;
  }

  public RuleExecutionInformation setRuleId(String ruleId) {
    this.ruleId = ruleId;
    return this;
  }

  public String getRuleDescription() {
    return ruleDescription;
  }

  public RuleExecutionInformation setRuleDescription(String ruleDescription) {
    this.ruleDescription = ruleDescription;
    return this;
  }

  public Map<String, Object> getRuleInput() {
    return ruleInput;
  }

  public RuleExecutionInformation setRuleInput(Map<String, Object> ruleInput) {
    this.ruleInput = ruleInput;
    return this;
  }

  public boolean wasTriggered() {
    return wasTriggered;
  }

  public RuleExecutionInformation setWasTriggered(boolean wasTriggered) {
    this.wasTriggered = wasTriggered;
    return this;
  }
}
