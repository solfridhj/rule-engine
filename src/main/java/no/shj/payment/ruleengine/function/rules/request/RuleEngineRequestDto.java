package no.shj.payment.ruleengine.function.rules.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RuleEngineRequestDto {

  private @NotNull String customerType;
  private @NotNull BigDecimal transactionAmount;
  private @NotNull String paymentMethod;
  private @NotNull String paymentOriginCountry;
  private @NotNull String paymentCurrency;
  private @NotNull String cardType;
}
