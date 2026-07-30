package dev.ohhoonim.system.accesscontrol.pdp.model;

import dev.ohhoonim.component.model.unit.DomainException;

/**
 * PdpException
 */
public class PdpException extends DomainException {
    public PdpException(String message) {
        super(message);
    }

    public PdpException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String errorCode() {
        return "PDP error";
    }

}
