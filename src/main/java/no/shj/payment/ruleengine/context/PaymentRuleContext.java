package no.shj.payment.ruleengine.context;

import com.neovisionaries.i18n.CountryCode;
import java.util.ArrayList;
import java.util.List;

public class PaymentRuleContext {

  private String customerType;
  // fixme: use money
  private String transactionAmount;
  private String paymentMethod;
  private CountryCode paymentOriginCountry;
  private String paymentCurrency;

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

  public CountryCode getPaymentOriginCountry() {
    return paymentOriginCountry;
  }

  public String getPaymentCurrency() {
    return paymentCurrency;
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
    private CountryCode paymentOriginCountry;
    private String paymentCurrency;

    private PaymentRuleContextBuilder() {}

    public static PaymentRuleContextBuilder builder() {
      return new PaymentRuleContextBuilder();
    }

    public PaymentRuleContextBuilder customerType(String customerType) {
      this.customerType = customerType.toUpperCase();
      return this;
    }

    public PaymentRuleContextBuilder transactionAmount(String transactionAmount) {
      this.transactionAmount = transactionAmount.toUpperCase();
      return this;
    }

    public PaymentRuleContextBuilder paymentMethod(String paymentMethod) {
      this.paymentMethod = paymentMethod.toUpperCase();
      return this;
    }

    public PaymentRuleContextBuilder paymentOriginCountry(CountryCode paymentOriginCountry) {
      this.paymentOriginCountry = paymentOriginCountry;
      return this;
    }

    public PaymentRuleContextBuilder paymentCurrency(String paymentCurrency) {
      this.paymentCurrency = paymentCurrency.toUpperCase();
      return this;
    }

    public PaymentRuleContext build() {
      PaymentRuleContext paymentRuleContext = new PaymentRuleContext();
      paymentRuleContext.customerType = this.customerType;
      paymentRuleContext.paymentMethod = this.paymentMethod;
      paymentRuleContext.paymentOriginCountry = this.paymentOriginCountry;
      paymentRuleContext.transactionAmount = this.transactionAmount;
      paymentRuleContext.paymentCurrency = this.paymentCurrency;
      return paymentRuleContext;
    }
  }

  // TODO - equals, hashmap. Lombok?
}
