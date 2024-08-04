package no.shj.payment.ruleengine.exception;

import org.springframework.http.ProblemDetail;

/** Top-level exception. */
public abstract class RuleEngineException extends RuntimeException {

  private ProblemDetail problemDetail;

  protected RuleEngineException(ProblemDetail problemDetail) {
    super(problemDetail.getTitle());
    this.problemDetail = problemDetail;
  }

  public ProblemDetail getProblemDetail() {
    return problemDetail;
  }
}
