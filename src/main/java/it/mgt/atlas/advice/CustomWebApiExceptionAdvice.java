package it.mgt.atlas.advice;

import it.mgt.atlas.exception.ExampleException;
import it.mgt.util.spring.web.exception.ExceptionPayload;
import it.mgt.util.spring.web.exception.WebApiExceptionAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
@ControllerAdvice
public class CustomWebApiExceptionAdvice extends WebApiExceptionAdvice {

    @ExceptionHandler({ExampleException.class})
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ResponseBody
    public Object exampleException(HttpServletRequest req, ExampleException e) {
        return new ExceptionPayload(e.getCode(), e.getMessage());
    }

}
