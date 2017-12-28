package it.mgt.atlas.exception;

import it.mgt.util.spring.exception.ErrorDefinition;

public enum Errors implements ErrorDefinition {

    EXAMPLE_ERROR("E01", "Example error");

    private final String code;
    private final String message;

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
