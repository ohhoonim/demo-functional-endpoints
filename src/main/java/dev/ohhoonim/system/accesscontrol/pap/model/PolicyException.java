package dev.ohhoonim.system.accesscontrol.pap.model;

import dev.ohhoonim.component.model.unit.DomainException;

public class PolicyException extends DomainException {
    public PolicyException(String message) {
        super(message);
    }

    public PolicyException(String message, Throwable e) {
        super(message, e);
    }
    @Override
    public String errorCode() {
        return "PolicyException";
    }
}