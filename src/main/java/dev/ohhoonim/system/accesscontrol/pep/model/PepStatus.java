package dev.ohhoonim.system.accesscontrol.pep.model;


public sealed interface PepStatus {

    record Intercepted() implements PepStatus {}
    record Evaluating() implements PepStatus {}
    record Permitted(PepComponent.EnforcementResult result) implements PepStatus {}
    record Denied(PepComponent.EnforcementResult result) implements PepStatus {}
    record Failed(String cause) implements PepStatus {}

    default boolean isIntercepted() {
        return this instanceof Intercepted;
    }

    default boolean isEvaluating() {
        return this instanceof Evaluating;
    }

    default boolean isPermitted() {
        return this instanceof Permitted;
    }

    default boolean isDenied() {
        return this instanceof Denied;
    }

    default boolean isFailed() {
        return this instanceof Failed;
    }
}