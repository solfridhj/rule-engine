package no.shj.payment.ruleengine.common;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReflectionsConfig {

  @Bean
  public Reflections reflection() {
    return new Reflections("no.shj.payment.ruleengine");
  }
}
