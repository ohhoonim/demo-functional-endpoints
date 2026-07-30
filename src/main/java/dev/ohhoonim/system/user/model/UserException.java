package dev.ohhoonim.system.user.model;

import dev.ohhoonim.component.model.unit.DomainException;

public class UserException extends DomainException{
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable e) {
        super(message, e);
    }

    @Override
    public String errorCode() {
        return "Uer error";
    }
}
