package no.shj.payment.ruleengine.testconfig;

import java.net.URL;
import java.nio.file.Paths;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class ReflectionsTestsConfig {

  public static Reflections createTestReflections() {
    try {
      String testClassesPath = Paths.get("target", "test-classes").toUri().toURL().toString();
      URL testClassesUrl = new URL(testClassesPath);
      return new Reflections(new ConfigurationBuilder().setUrls(testClassesUrl));
    } catch (Exception e) {
      throw new RuntimeException("Cannot create test config for reflection");
    }
  }
}
