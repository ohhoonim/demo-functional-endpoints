package dev.ohhoonim.system.accesscontrol.pip.model;

import dev.ohhoonim.component.model.unit.DomainException;

/**
 * PipException
 */
public class PipException extends DomainException {

    public PipException(String message) {
        super(message);
    }

    public PipException(String message, Throwable e) {
        super(message, e);
    }


    @Override
    public String errorCode() {
        return "PIP error";
    }

}
