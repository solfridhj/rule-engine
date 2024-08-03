package no.shj.payment.ruleengine.function.rules.response;

import java.math.BigDecimal;
import java.util.List;

public class RuleEngineResponseDto {

  String paymentMethod;
  String acquirerId;
  Boolean shouldWaive3ds;
  Boolean manualVerificationRequired;
  BigDecimal calculatedFee;
  String calculatedFeeCurrency;
  List<ExecutionInformationDto> executionInformationList;

  public static final class RuleEngineResponseDtoBuilder {
    private String paymentMethod;
    private String acquirerId;
    private Boolean shouldWaive3ds;
    private Boolean manualVerificationRequired;
    private BigDecimal calculatedFee;
    private String calculatedFeeCurrency;
    private List<ExecutionInformationDto> executionInformationList;

    private RuleEngineResponseDtoBuilder() {}

    public static RuleEngineResponseDtoBuilder builder() {
      return new RuleEngineResponseDtoBuilder();
    }

    public RuleEngineResponseDtoBuilder paymentMethod(String paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public RuleEngineResponseDtoBuilder acquirerId(String acquirerId) {
      this.acquirerId = acquirerId;
      return this;
    }

    public RuleEngineResponseDtoBuilder shouldWaive3ds(Boolean shouldWaive3ds) {
      this.shouldWaive3ds = shouldWaive3ds;
      return this;
    }

    public RuleEngineResponseDtoBuilder manualVerificationRequired(
        Boolean manualVerificationRequired) {
      this.manualVerificationRequired = manualVerificationRequired;
      return this;
    }

    public RuleEngineResponseDtoBuilder calculatedFee(BigDecimal calculatedFee) {
      this.calculatedFee = calculatedFee;
      return this;
    }

    public RuleEngineResponseDtoBuilder calculatedFeeCurrency(String calculatedFeeCurrency) {
      this.calculatedFeeCurrency = calculatedFeeCurrency;
      return this;
    }

    public RuleEngineResponseDtoBuilder executionInformationList(
        List<ExecutionInformationDto> executionInformationList) {
      this.executionInformationList = executionInformationList;
      return this;
    }

    public RuleEngineResponseDto build() {
      RuleEngineResponseDto ruleEngineResponseDto = new RuleEngineResponseDto();
      ruleEngineResponseDto.executionInformationList = this.executionInformationList;
      ruleEngineResponseDto.shouldWaive3ds = this.shouldWaive3ds;
      ruleEngineResponseDto.paymentMethod = this.paymentMethod;
      ruleEngineResponseDto.calculatedFee = this.calculatedFee;
      ruleEngineResponseDto.manualVerificationRequired = this.manualVerificationRequired;
      ruleEngineResponseDto.acquirerId = this.acquirerId;
      ruleEngineResponseDto.calculatedFeeCurrency = this.calculatedFeeCurrency;
      return ruleEngineResponseDto;
    }
  }
}
