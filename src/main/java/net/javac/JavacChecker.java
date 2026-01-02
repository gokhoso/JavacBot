package net.javac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JavacChecker {
    public record Checker(boolean fail, String message) {}
    private final Set<Checker> checker = new HashSet<>();
    private final Logger log = LoggerFactory.getLogger(JavacChecker.class);

    public JavacChecker addMinSizeCheck(int size, int min, String name) {
        checker.add(new Checker(size < min, name + " must be greater than " + min));
        return this;
    }

    public JavacChecker addMaxSizeCheck(int size, int max, String name) {
        checker.add(new Checker(size > max, name + " must be lower than " + max));
        return this;
    }

    @SuppressWarnings("unused")
    public JavacChecker addEqualCheck(Object a, Object b) {
        checker.add(new Checker(!Objects.equals(a, b), a + " and " + b + " is not equal"));
        return this;
    }

    public JavacChecker addEqualCheck(Object a, Object b, String message) {
        checker.add(new Checker(!Objects.equals(a, b), message));
        return this;
    }

    public JavacChecker addPlaceHolderCheck(String value) {
        checker.add(new Checker(value.startsWith("Enter your"), "Place Holder Value is used for " + value + "."));
        return this;
    }

    public JavacChecker addPlaceHolderCheck(String value, String message) {
        checker.add(new Checker(value.startsWith("Enter your"), message));
        return this;
    }


    public void check() {
        checker.forEach(check -> {
            if (check.fail) {
                log.error(check.message);
                throw new IllegalArgumentException(check.message);
            }
        });
    }
}
