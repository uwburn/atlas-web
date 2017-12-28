package it.mgt.atlas.exception;

import it.mgt.util.spring.exception.CodedException;

public class UserCreationException extends CodedException {

    public UserCreationException(String code, String message) {
        super(code, message);
    }

}
