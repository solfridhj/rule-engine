package no.shj.payment.ruleengine.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.net.URI;
import java.util.Optional;
import no.shj.payment.ruleengine.function.request.RuleEngineRequestDto;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class EngineRuleHandler {

  private final RuleExecutionFunction executionFunction;

  public EngineRuleHandler(RuleExecutionFunction executionFunction) {
    this.executionFunction = executionFunction;
  }

  @FunctionName("payments")
  public HttpResponseMessage evaluateRules(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.POST},
              authLevel = AuthorizationLevel.ANONYMOUS)
          HttpRequestMessage<Optional<RuleEngineRequestDto>> request,
      ExecutionContext context) {

    if (request.getBody().isEmpty()) {
      return request.createResponseBuilder(HttpStatus.BAD_REQUEST).build();
    }
    var input = request.getBody().get();
    try {
      executionFunction.apply(input);
      return request
          .createResponseBuilder(HttpStatus.OK)
          .body(executionFunction.apply(input))
          .header("Content-Type", "application/json")
          .build();
    } catch (Exception e) {
      // TODO - could be a lot better
      var problemDetail =
          ProblemDetail.forStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
      problemDetail.setInstance(URI.create("/payments"));
      problemDetail.setDetail(e.getMessage());
      return request
          .createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(ProblemDetail.forStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR))
          .header("Content-Type", "application/problem+json")
          .build();
    }
  }
}
