package org.example.utils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;

/**
 * @author luigi
 * 17/02/2023
 */
public final class LambdaUtils {
    //TODO Change for Log4J
    public static LambdaLogger initializeLogger(Context context) {
        LambdaLogger logger;
        if (context.getLogger() != null) {
            logger = context.getLogger();
        } else {
            logger = LambdaRuntime.getLogger();
        }
        return logger;
    }
}
