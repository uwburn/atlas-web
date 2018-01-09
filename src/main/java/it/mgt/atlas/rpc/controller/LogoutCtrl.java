package it.mgt.atlas.rpc.controller;

import it.mgt.atlas.service.AuthSvc;
import it.mgt.util.spring.auth.AuthSession;
import it.mgt.util.spring.web.auth.SessionTokenInterceptor;
import it.mgt.util.spring.web.exception.NotFoundException;
import it.mgt.util.spring.web.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RequestMapping("/logout")
@Controller
public class LogoutCtrl {

    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private SessionTokenInterceptor sessionTokenInterceptor;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void procedure(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (sessionTokenInterceptor.getCookieName().equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null)
            throw new UnauthorizedException();

        AuthSession session = authSvc.getValidAuthSession(token);

        if (session == null)
            throw new NotFoundException();

        authSvc.invalidateSession(session);
    }

}
