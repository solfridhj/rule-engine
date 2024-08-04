package no.shj.payment.ruleengine.ruleservice;

import no.shj.payment.ruleengine.database.RuleConfigurationDaoImpl;
import no.shj.payment.ruleengine.ruleservice.context.PaymentRuleContext;
import no.shj.payment.ruleengine.ruleservice.exception.PaymentRuleEngineException;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import no.shj.payment.ruleengine.ruleservice.rules.AcquirerRoutingRule;
import no.shj.payment.ruleengine.ruleservice.rules.FeeCalculationRule;
import no.shj.payment.ruleengine.ruleservice.rules.PaymentMethodRule;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.Set;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class RuleSetExecutionService {

  private ApplicationContext applicationContext;

  public RuleSetExecutionService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void evaluateRules(PaymentRuleContext context) {

    Reflections reflections = new Reflections("no.shj.payment.ruleengine");
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
