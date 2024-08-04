package no.shj.payment.ruleengine.function.rules.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
