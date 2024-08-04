package no.shj.payment.ruleengine.function;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import no.shj.payment.ruleengine.exception.RuleEngineException;
import no.shj.payment.ruleengine.function.ruleconfig.RuleConfigExecutionFunction;
import no.shj.payment.ruleengine.function.ruleconfig.RuleConfigSchemaFunction;
import no.shj.payment.ruleengine.function.ruleconfig.UpdateRuleExecutionFunction;
import no.shj.payment.ruleengine.function.ruleconfig.updaterule.UpdateRuleConfigurationRequestDto;
import no.shj.payment.ruleengine.function.rules.RuleExecutionFunction;
import no.shj.payment.ruleengine.function.rules.request.RuleEngineRequestDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class EngineRuleHandler {

  private final RuleExecutionFunction executionFunction;
  private final RuleConfigExecutionFunction ruleConfigExecutionFunction;
  private final UpdateRuleExecutionFunction updateRuleExecutionFunction;
  private final RuleConfigSchemaFunction ruleConfigSchemaFunction;

  public EngineRuleHandler(
      RuleExecutionFunction executionFunction,
      RuleConfigExecutionFunction ruleConfigExecutionFunction,
      UpdateRuleExecutionFunction updateRuleExecutionFunction,
      RuleConfigSchemaFunction ruleConfigSchemaFunction) {
    this.executionFunction = executionFunction;
    this.ruleConfigExecutionFunction = ruleConfigExecutionFunction;
    this.updateRuleExecutionFunction = updateRuleExecutionFunction;
    this.ruleConfigSchemaFunction = ruleConfigSchemaFunction;
  }

  @FunctionName("evaluateRules")
  public HttpResponseMessage evaluateRules(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.POST},
              authLevel = AuthorizationLevel.ANONYMOUS,
              route = "payments/rules")
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
      var problemDetail =
          ProblemDetail.forStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
      problemDetail.setDetail(e.getMessage());
      return request
          .createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(problemDetail)
          .header("Content-Type", "application/problem+json")
          .build();
    }
  }

  @FunctionName("getAllRules")
  public HttpResponseMessage getAllRules(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.GET},
              authLevel = AuthorizationLevel.ANONYMOUS,
              route = "payments/rules")
          HttpRequestMessage<Optional<String>> request,
      ExecutionContext context) {
    var result = ruleConfigExecutionFunction.apply(null);
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(result)
        .header("Content-Type", "application/json")
        .build();
  }

  @FunctionName("getRuleConfigSchema")
  public HttpResponseMessage getRuleConfigSchema(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.GET},
              authLevel = AuthorizationLevel.ANONYMOUS,
              route = "payments/configurations")
          HttpRequestMessage<Optional<String>> request,
      ExecutionContext context) {
    var result = ruleConfigSchemaFunction.apply(null);
    return request
        .createResponseBuilder(HttpStatus.OK)
        .body(result)
        .header("Content-Type", "application/json")
        .build();
  }

  @FunctionName("updateRuleConfiguration")
  public HttpResponseMessage updateRuleConfiguration(
      @HttpTrigger(
              name = "request",
              methods = {HttpMethod.POST},
              authLevel = AuthorizationLevel.ANONYMOUS,
              route = "payments/configurations")
          HttpRequestMessage<Optional<UpdateRuleConfigurationRequestDto>> request,
      ExecutionContext context) {

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      Object requestAsObject = request.getBody().get();
      UpdateRuleConfigurationRequestDto configObject =
          objectMapper.convertValue(requestAsObject, UpdateRuleConfigurationRequestDto.class);

      var result = updateRuleExecutionFunction.apply(configObject);

      return request
          .createResponseBuilder(HttpStatus.CREATED)
          .body(result)
          .header("Content-Type", "application/json")
          .build();

    } catch (RuleEngineException e) {
      var problemDetail = e.getProblemDetail();
      return request
          .createResponseBuilder(HttpStatus.valueOf(problemDetail.getStatus()))
          .body(problemDetail)
          .header("Content-Type", "application/problem+json")
          .build();
    } catch (ConstraintViolationException e) {
      var problemDetail = createProblemDetailWithConstraintInformation(e);
      return request
          .createResponseBuilder(HttpStatus.valueOf(problemDetail.getStatus()))
          .body(problemDetail)
          .header("Content-Type", "application/problem+json")
          .build();
    } catch (Exception e) {
      var problemDetail =
          ProblemDetail.forStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
      problemDetail.setDetail(e.getMessage());
      return request
          .createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(problemDetail)
          .header("Content-Type", "application/problem+json")
          .build();
    }
  }

  private @NotNull ProblemDetail createProblemDetailWithConstraintInformation(
      ConstraintViolationException e) {
    var problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
    problemDetail.setTitle("Bad Request");
    problemDetail.setDetail("Validation of rule configuration input failed.");
    var constraintViolations = e.getConstraintViolations();
    Map<String, Object> violationsMap =
        constraintViolations.stream()
            .collect(
                Collectors.toMap(
                    violation -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage,
                    (existing, replacement) ->
                        existing // Keep the existing value if a duplicate key is encountered
                    ));
    problemDetail.setProperties(violationsMap);
    return problemDetail;
  }
}
