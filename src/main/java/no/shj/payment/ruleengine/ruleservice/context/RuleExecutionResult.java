package no.shj.payment.ruleengine.ruleservice.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.javamoney.moneta.Money;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
// Fields will be null if rule was not executed
public class RuleExecutionResult {
  private String paymentMethod;
  private String acquirerId;
  private Boolean shouldWaive3ds;
  private Boolean manualVerificationRequired;
  private Money calculatedFee;
}
