package no.shj.payment.ruleengine.engine;


import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EngineRuleHandler {

    private RuleExecutionFunction executionFunction;

    public EngineRuleHandler(RuleExecutionFunction executionFunction) {
        this.executionFunction = executionFunction;
    }

    @FunctionName("executionFunction")
    public HttpResponseMessage execute(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST, HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {

        if (request.getBody().isEmpty()) {
            return request
                    .createResponseBuilder(HttpStatus.OK)
                    .body(executionFunction.apply("Hei verden!! "))
                    .header("Content-Type", "application/json")
                    .build();
        }
        var input = request.getBody().get();
        context.getLogger().info("Payment has started processing: " + input);

        // Do the rule evaluation

        return request
                .createResponseBuilder(HttpStatus.OK)
                .body(executionFunction.apply(input))
                .header("Content-Type", "application/json")
                .build();
    }
}