package io.ecocode.java.checks.sobriety;

import io.ecocode.java.checks.sobriety.VibrationFreeRule;
import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class VibrationFreeRuleTest {

    @Test
    public void verify() {

        JavaCheckVerifier.newVerifier().onFile("src/test/files/sobriety/VibrationFreeCheckContext.java")
                .withChecks(new VibrationFreeRule("android.content.Context", "vibrator"),
                        new VibrationFreeRule("android.content.Context", "vibrator_manager"))
                .verifyIssues();

        JavaCheckVerifier.newVerifier().onFile("src/test/files/sobriety/VibrationFreeCheckActivity.java")
                .withChecks(new VibrationFreeRule("android.app.Activity", "vibrator"),
                        new VibrationFreeRule("android.app.Activity", "vibrator_manager"))
                .verifyIssues();
    }

}
