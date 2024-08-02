package no.shj.payment.ruleengine.engine;

import org.springframework.stereotype.Component;

import java.util.function.Function;

// TODO - starting point here - could also have mapped to the context at this point.
//  Intended to be portable so I can use this for a Lambda in the future.
@Component
public class RuleExecutionFunction implements Function<String, String> {

    @Override
    public String apply(String str) {
        return str;
    }
}
