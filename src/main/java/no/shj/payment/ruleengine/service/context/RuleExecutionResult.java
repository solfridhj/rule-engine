package no.shj.payment.ruleengine.service.context;

import org.javamoney.moneta.Money;

// Fields will be null if rule was not executed.
public class RuleExecutionResult {

  String paymentMethod;
  String acquirerId;
  Boolean shouldWaive3ds;
  Boolean manualVerificationRequired;
  Money calculatedFee;

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public RuleExecutionResult setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  public String getAcquirerId() {
    return acquirerId;
  }

  public RuleExecutionResult setAcquirerId(String acquirerId) {
    this.acquirerId = acquirerId;
    return this;
  }

  public Boolean getShouldWaive3ds() {
    return shouldWaive3ds;
  }

  public RuleExecutionResult setShouldWaive3ds(Boolean shouldWaive3ds) {
    this.shouldWaive3ds = shouldWaive3ds;
    return this;
  }

  public Boolean getManualVerificationRequired() {
    return manualVerificationRequired;
  }

  public RuleExecutionResult setManualVerificationRequired(Boolean manualVerificationRequired) {
    this.manualVerificationRequired = manualVerificationRequired;
    return this;
  }

  public Money getCalculatedFee() {
    return calculatedFee;
  }

  public RuleExecutionResult setCalculatedFee(Money calculatedFee) {
    this.calculatedFee = calculatedFee;
    return this;
  }
}
