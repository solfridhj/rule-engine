package no.shj.payment.ruleengine.function.rules.mapper;

import static java.util.Optional.ofNullable;

import com.neovisionaries.i18n.CountryCode;
import jakarta.validation.Valid;
import javax.money.CurrencyUnit;
import no.shj.payment.ruleengine.function.rules.request.RuleEngineRequestDto;
import no.shj.payment.ruleengine.function.rules.response.ExecutionInformationDto;
import no.shj.payment.ruleengine.function.rules.response.RuleEngineResponseDto;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.context.RuleExecutionInformation;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ContextDtoMapper {

  public @Valid PaymentRuleContext map(@Valid RuleEngineRequestDto requestDto) {
    return PaymentRuleContext.PaymentRuleContextBuilder.builder()
        .transactionAmount(requestDto.getTransactionAmount())
        .paymentOriginCountry(CountryCode.valueOf(requestDto.getPaymentOriginCountry()))
        .customerType(requestDto.getCustomerType())
        .paymentMethod(requestDto.getPaymentMethod())
        .paymentCurrency(requestDto.getPaymentCurrency())
        .cardType(requestDto.getCardType())
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
        .executionInformationList(
            context.getRuleExecutionInformationList().stream().map(this::map).toList())
        .build();
  }

  private ExecutionInformationDto map(RuleExecutionInformation information) {
    return ExecutionInformationDto.ExecutionInformationBuilder.builder()
        .ruleId(String.valueOf(information.getRuleId()))
        .ruleDescription(information.getRuleDescription())
        .wasTriggered(information.isWasTriggered())
        .ruleInput(information.getRuleInput())
        .build();
  }
}
