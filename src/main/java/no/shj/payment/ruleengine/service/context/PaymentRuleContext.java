package no.shj.payment.ruleengine.service.context;

import com.neovisionaries.i18n.CountryCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentRuleContext {

  private String customerType;
  private BigDecimal transactionAmount;
  private String paymentMethod;
  private CountryCode paymentOriginCountry;
  private String paymentCurrency;
  private String cardType;

  // Output
  private final List<RuleExecutionInformation> ruleExecutionInformationList = new ArrayList<>();
  private final RuleExecutionResult ruleExecutionResult = new RuleExecutionResult();

  public String getCustomerType() {
    return customerType;
  }

  public BigDecimal getTransactionAmount() {
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

  public String getCardType() {
    return cardType;
  }

  public List<RuleExecutionInformation> getRuleExecutionInformationList() {
    return ruleExecutionInformationList;
  }

  public RuleExecutionResult getRuleExecutionResult() {
    return ruleExecutionResult;
  }

  public static final class PaymentRuleContextBuilder {
    private String customerType;
    private BigDecimal transactionAmount;
    private String paymentMethod;
    private CountryCode paymentOriginCountry;
    private String paymentCurrency;
    private String cardType;

    private PaymentRuleContextBuilder() {}

    public static PaymentRuleContextBuilder builder() {
      return new PaymentRuleContextBuilder();
    }

    public PaymentRuleContextBuilder customerType(String customerType) {
      this.customerType = customerType.toUpperCase();
      return this;
    }

    public PaymentRuleContextBuilder transactionAmount(BigDecimal transactionAmount) {
      this.transactionAmount = transactionAmount;
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

    public PaymentRuleContextBuilder cardType(String cardType) {
      this.cardType = cardType.toUpperCase();
      return this;
    }

    public PaymentRuleContext build() {
      PaymentRuleContext paymentRuleContext = new PaymentRuleContext();
      paymentRuleContext.customerType = this.customerType;
      paymentRuleContext.paymentMethod = this.paymentMethod;
      paymentRuleContext.paymentOriginCountry = this.paymentOriginCountry;
      paymentRuleContext.transactionAmount = this.transactionAmount;
      paymentRuleContext.paymentCurrency = this.paymentCurrency;
      paymentRuleContext.cardType = this.cardType;
      return paymentRuleContext;
    }
  }

  // TODO - equals, hashmap. Lombok?
}
