package no.shj.payment.ruleengine.ruleservice.exception;

import no.shj.payment.ruleengine.exception.RuleEngineException;
import org.springframework.http.ProblemDetail;

public class PaymentRuleEngineException extends RuleEngineException {

  public PaymentRuleEngineException(ProblemDetail problemDetail) {
    super(problemDetail);
  }
}
