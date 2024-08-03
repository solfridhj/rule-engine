package no.shj.payment.ruleengine;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import no.shj.payment.ruleengine.ruleservice.genericengine.AbstractRule;
import no.shj.payment.ruleengine.ruleservice.genericengine.RuleMetadata;
import org.junit.jupiter.api.Test;

class AppArchitectureTest {

  JavaClasses javaClasses = new ClassFileImporter().importPackages("no.shj.payment.ruleengine");

  @Test
  void allRulesShouldHaveMetadata() {
    var rule =
        classes()
            .that()
            .areAssignableTo(AbstractRule.class)
            .should()
            .beAnnotatedWith(RuleMetadata.class)
            .orShould()
            .haveSimpleName("AbstractRule"); // The abstract rule itself is the only exception.

    rule.check(javaClasses);
  }
}
