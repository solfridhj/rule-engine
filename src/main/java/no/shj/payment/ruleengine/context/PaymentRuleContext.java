package no.shj.payment.ruleengine.context;

import java.util.ArrayList;
import java.util.List;

public class PaymentRuleContext {

  private String customerType;
  private String transactionAmount;
  private String paymentMethod;
  private String paymentOriginCountry;

  // Output
  private final List<RuleExecutionInformation> ruleExecutionInformationList = new ArrayList<>();
  private final RuleExecutionResult ruleExecutionResult = new RuleExecutionResult();

  public String getCustomerType() {
    return customerType;
  }

  public String getTransactionAmount() {
    return transactionAmount;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public String getPaymentOriginCountry() {
    return paymentOriginCountry;
  }

  public List<RuleExecutionInformation> getRuleExecutionInformationList() {
    return ruleExecutionInformationList;
  }

  public RuleExecutionResult getRuleExecutionResult() {
    return ruleExecutionResult;
  }

  public static final class PaymentRuleContextBuilder {
    private String customerType;
    private String transactionAmount;
    private String paymentMethod;
    private String paymentOriginCountry;

    private PaymentRuleContextBuilder() {}

    public static PaymentRuleContextBuilder Builder() {
      return new PaymentRuleContextBuilder();
    }

    public PaymentRuleContextBuilder customerType(String customerType) {
      this.customerType = customerType;
      return this;
    }

    public PaymentRuleContextBuilder transactionAmount(String transactionAmount) {
      this.transactionAmount = transactionAmount;
      return this;
    }

    public PaymentRuleContextBuilder paymentMethod(String paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public PaymentRuleContextBuilder paymentOriginCountry(String paymentOriginCountry) {
      this.paymentOriginCountry = paymentOriginCountry;
      return this;
    }

    public PaymentRuleContext build() {
      PaymentRuleContext paymentRuleContext = new PaymentRuleContext();
      paymentRuleContext.customerType = this.customerType;
      paymentRuleContext.paymentMethod = this.paymentMethod;
      paymentRuleContext.paymentOriginCountry = this.paymentOriginCountry;
      paymentRuleContext.transactionAmount = this.transactionAmount;
      return paymentRuleContext;
    }
  }

  // TODO - equals, hashmap. Lombok?
}
