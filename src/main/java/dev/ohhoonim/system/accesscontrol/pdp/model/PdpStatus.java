package dev.ohhoonim.system.accesscontrol.pdp.model;


public sealed interface PdpStatus {

    record Permit(String reason) implements PdpStatus {}
    record Deny(String reason) implements PdpStatus {}
    record Indeterminate(String cause) implements PdpStatus {}
    record NotApplicable(String reason) implements PdpStatus {}

    default boolean isPermit() {
        return this instanceof Permit;
    }

    default boolean isDeny() {
        return this instanceof Deny;
    }

    default boolean isIndeterminate() {
        return this instanceof Indeterminate;
    }

    default boolean isNotApplicable() {
        return this instanceof NotApplicable;
    }
}
