package no.shj.payment.ruleengine.function;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.net.URI;
import java.util.Optional;
import no.shj.payment.ruleengine.function.ruleconfig.RuleConfigExecutionFunction;
import no.shj.payment.ruleengine.function.rules.request.RuleEngineRequestDto;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class EngineRuleHandler {

  private final RuleExecutionFunction executionFunction;
  private final RuleConfigExecutionFunction ruleConfigExecutionFunction;

  public EngineRuleHandler(
      RuleExecutionFunction executionFunction,
      RuleConfigExecutionFunction ruleConfigExecutionFunction) {
    this.executionFunction = executionFunction;
    this.ruleConfigExecutionFunction = ruleConfigExecutionFunction;
  }

  @FunctionName("payments")
  public HttpResponseMessage evaluateRules(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.POST},
              authLevel = AuthorizationLevel.ANONYMOUS)
          HttpRequestMessage<Optional<RuleEngineRequestDto>> request) {

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

  @FunctionName("payments-configuration")
  public HttpResponseMessage getAllRules(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.GET},
              authLevel = AuthorizationLevel.ANONYMOUS)
          HttpRequestMessage<Optional<String>> request,
      ExecutionContext context) {
    var result = ruleConfigExecutionFunction.apply(null);
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(result)
        .header("Content-Type", "application/json")
        .build();
  }
}
