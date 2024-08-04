package no.shj.payment.ruleengine.configservice;

import no.shj.payment.ruleengine.exception.RuleEngineException;
import org.springframework.http.ProblemDetail;

public class RuleConfigException extends RuleEngineException {

  public RuleConfigException(ProblemDetail problemDetail) {
    super(problemDetail);
  }
}
