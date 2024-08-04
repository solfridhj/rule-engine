package no.shj.payment.ruleengine.ruleservice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Set;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.exception.PaymentRuleEngineException;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;

@Service
public class RuleSetExecutionService {

  private final ApplicationContext applicationContext;
  private final Reflections reflections;

  public RuleSetExecutionService(ApplicationContext applicationContext, Reflections reflections) {
    this.applicationContext = applicationContext;
    this.reflections = reflections;
  }

  public void evaluateRules(PaymentRuleContext context) {

    Set<Class<? extends AbstractRule>> ruleClasses = reflections.getSubTypesOf(AbstractRule.class);

    for (Class<? extends AbstractRule> ruleClass : ruleClasses) {
      try {
        var actualRuleBean = applicationContext.getBean(ruleClass);
        actualRuleBean.executeRule(context);
      } catch (Exception e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
        problemDetail.setDetail(e.getMessage());
        throw new PaymentRuleEngineException(problemDetail);
      }
    }
  }
}
