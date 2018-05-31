package ru.spbau.alferov.javahw.myjunit.launcher.target;

import com.google.common.base.Stopwatch;
import org.jetbrains.annotations.NotNull;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationFailure;
import ru.spbau.alferov.javahw.myjunit.launcher.InvocationIgnored;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;

public class HelperMethod<T> extends TargetMethod<T> {
    public HelperMethod(@NotNull Method ofMethod, @NotNull CallWhen targetType) {
        super(ofMethod, targetType);
    }

    @Override
    public Duration invoke(T onObject) throws InvocationFailure, InvocationIgnored {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            ofMethod.invoke(onObject);
            stopwatch.stop();
            return stopwatch.elapsed();
        } catch (IllegalAccessException e) {
            // Checking the invocation on being allowed is performed in {@link TargetMethod#createTargetMethod}, thus
            // this situation is clearly a bug.
            throw new RuntimeException("BUG: IllegalAccessException thrown on a checked method", e);
        } catch (InvocationTargetException e) {
            throw new InvocationFailure(stopwatch.elapsed(), "Invocation of method " + ofMethod.getName() + " finished with unexpected exception" + e.getCause());
        }
    }
}
