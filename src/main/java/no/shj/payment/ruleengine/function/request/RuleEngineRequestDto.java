package no.shj.payment.ruleengine.function.request;

import jakarta.validation.constraints.NotNull;

// TODO - check if I can have request with builder of if I have to have setter.
public class RuleEngineRequestDto {

  private @NotNull String customerType;
  private @NotNull String transactionAmount;
  private @NotNull String paymentMethod;
  private @NotNull String paymentOriginCountry;
  private @NotNull String paymentCurrency;

  public String getCustomerType() {
    return customerType;
  }

  public RuleEngineRequestDto setCustomerType(String customerType) {
    this.customerType = customerType;
    return this;
  }

  public String getTransactionAmount() {
    return transactionAmount;
  }

  public RuleEngineRequestDto setTransactionAmount(String transactionAmount) {
    this.transactionAmount = transactionAmount;
    return this;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public RuleEngineRequestDto setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  public String getPaymentOriginCountry() {
    return paymentOriginCountry;
  }

  public RuleEngineRequestDto setPaymentOriginCountry(String paymentOriginCountry) {
    this.paymentOriginCountry = paymentOriginCountry;
    return this;
  }

  public @NotNull String getPaymentCurrency() {
    return paymentCurrency;
  }

  public RuleEngineRequestDto setPaymentCurrency(@NotNull String paymentCurrency) {
    this.paymentCurrency = paymentCurrency;
    return this;
  }
}
