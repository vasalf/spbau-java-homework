package ru.spbau.alferov.javahw.myjunit.launcher.target;

import com.google.common.base.Stopwatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationFailure;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationIgnored;
import ru.spbau.alferov.javahw.myjunit.test.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;

/**
 * This is an override of {@link TargetMethod} for test methods
 *
 * @param <T> The class from which the test is taken.
 */
public class TestMethod<T> extends TargetMethod<T> {
    /**
     * Base constructor. Refer to {@link TargetMethod#TargetMethod(Method, CallWhen)}
     */
    public TestMethod(Method method, CallWhen callWhen) {
        super(method, callWhen);
    }

    /**
     * Invokes the test on given object.
     *
     * @param onObject On which object to invoke
     * @return Time elapsed
     * @throws InvocationFailure In case of unexpected exception in the test
     * @throws InvocationIgnored In case of ignored test
     */
    @NotNull
    @Override
    public Duration invoke(T onObject) throws InvocationFailure, InvocationIgnored {
        @NotNull Test annotation = ofMethod.getAnnotation(Test.class);

        if (!annotation.ignored().equals("")) {
            throw new InvocationIgnored(annotation.ignored());
        } else {
            @Nullable Throwable exitReason = null;
            Stopwatch stopwatch = Stopwatch.createStarted();
            try {
                ofMethod.invoke(onObject);
            } catch (IllegalAccessException e) {
                // Checking the invocation on being allowed is performed in {@link TargetMethod#createTargetMethod},
                // thus this situation is clearly a bug.
                throw new RuntimeException("BUG: IllegalAccessException thrown on a checked method", e);
            } catch (InvocationTargetException e) {
                exitReason = e.getCause();
            }
            stopwatch.stop();
            Duration result = stopwatch.elapsed();

            if (annotation.expected() == Test.NoExceptionHolder.class) {
                if (exitReason != null) {
                    throw new InvocationFailure(result, "Unexpected exception", exitReason);
                }
            } else {
                if (exitReason == null) {
                    throw new InvocationFailure(result, "No exception thrown, expected " + annotation.expected().getName());
                }
                if (!(annotation.expected().isInstance(exitReason))) {
                    throw new InvocationFailure(result, "Unexpected exception", exitReason);
                }
            }
            return result;
        }
    }
}