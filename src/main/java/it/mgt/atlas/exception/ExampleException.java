package it.mgt.atlas.exception;

import it.mgt.util.spring.exception.CodedException;

public class ExampleException extends CodedException {

    public ExampleException(String code, String message) {
        super(code, message);
    }

}
