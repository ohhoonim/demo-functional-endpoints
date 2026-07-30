package dev.ohhoonim.system.accesscontrol.pep.model;

import dev.ohhoonim.component.model.unit.DomainException;

/**
 * PepException
 */
public class PepException extends DomainException {

    public PepException(String message) {
        super(message);
    }
    public PepException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String errorCode() {
        return "PEP error";
    }

}
