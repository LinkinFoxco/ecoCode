package io.ecocode.java.checks.sobriety;

import io.ecocode.java.checks.helpers.constant.FlagOnMethodCheck;
import org.sonar.check.Rule;

@Rule(key = "ESOB011", name = "ecocodeVibrationFree")
public class VibrationFreeRule extends FlagOnMethodCheck {
    /**
     * Constructor to configure the rule on a given class and method.
     *
     * @param methodName           name of the method to check
     * @param methodOwnerType      name of the type that own the method
     * @param constantValueToCheck the constant value to check
     * @param paramPositions       the position(s) of the argument on the method to check
     */
    protected VibrationFreeRule(String methodName, String methodOwnerType, int constantValueToCheck, int... paramPositions) {
        super(methodName, methodOwnerType, constantValueToCheck, paramPositions);
    }

    @Override
    public String getMessage() {
        return "Prefer not using VIBRATOR_SERVICE in API 26";
    }
}
