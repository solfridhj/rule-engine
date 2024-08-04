package no.shj.payment.ruleengine.ruleservice.context;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import no.shj.payment.ruleengine.ruleservice.rules.Rule;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class RuleExecutionInformation {

  private Rule ruleId;
  private String ruleDescription;
  private Map<String, Object> ruleInput = new HashMap<>();
  private boolean wasTriggered = false;
}
