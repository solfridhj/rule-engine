package no.shj.payment.ruleengine.function.mapper;

import static java.util.Optional.ofNullable;

import jakarta.validation.Valid;
import javax.money.CurrencyUnit;
import no.shj.payment.ruleengine.context.PaymentRuleContext;
import no.shj.payment.ruleengine.context.RuleExecutionInformation;
import no.shj.payment.ruleengine.function.request.RuleEngineRequestDto;
import no.shj.payment.ruleengine.function.response.ExecutionInformationDto;
import no.shj.payment.ruleengine.function.response.RuleEngineResponseDto;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ContextDtoMapper {

  public @Valid PaymentRuleContext map(@Valid RuleEngineRequestDto requestDto) {
    return PaymentRuleContext.PaymentRuleContextBuilder.Builder()
        .transactionAmount(requestDto.getTransactionAmount())
        .paymentOriginCountry(requestDto.getPaymentOriginCountry())
        .customerType(requestDto.getCustomerType())
        .paymentMethod(requestDto.getPaymentMethod())
        .build();
  }

  public @Valid RuleEngineResponseDto map(@Valid PaymentRuleContext context) {
    var executionResult = context.getRuleExecutionResult();
    return RuleEngineResponseDto.RuleEngineResponseDtoBuilder.builder()
        .acquirerId(executionResult.getAcquirerId())
        .calculatedFee(
            ofNullable(executionResult.getCalculatedFee())
                .map(Money::getNumberStripped)
                .orElse(null))
        .calculatedFeeCurrency(
            ofNullable(executionResult.getCalculatedFee())
                .map(Money::getCurrency)
                .map(CurrencyUnit::getCurrencyCode)
                .orElse(null))
        .manualVerificationRequired(executionResult.getManualVerificationRequired())
        .shouldWaive3ds(executionResult.getShouldWaive3ds())
        .paymentMethod(executionResult.getPaymentMethod())
        .executionInformationDtoList(
            context.getRuleExecutionInformationList().stream().map(this::map).toList())
        .build();
  }

  private ExecutionInformationDto map(RuleExecutionInformation information) {
    return ExecutionInformationDto.ExecutionInformationBuilder.builder()
        .ruleId(information.getRuleId())
        .ruleDescription(information.getRuleDescription())
        .wasTriggered(information.wasTriggered())
        .ruleInput(information.getRuleInput())
        .build();
  }
}
