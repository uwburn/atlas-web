package it.mgt.atlas.rpc.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import it.mgt.util.spring.web.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import it.mgt.atlas.entity.Session;
import it.mgt.atlas.entity.User;
import it.mgt.atlas.service.AuthSvc;
import it.mgt.atlas.view.UserView;

import it.mgt.util.spring.web.auth.AuthUserInj;
import it.mgt.util.spring.web.auth.SessionTokenInterceptor;

@RequestMapping("/login")
@Controller
public class LoginCtrl {

    @Autowired
    private AuthSvc authSvc;

    @Autowired
    private SessionTokenInterceptor sessionTokenInterceptor;
    
    public static class Input {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    public static class Output {
        
        private Session session;

        public Output(Session session) {
            this.session = session;
        }
        
        public Session getSession() {
            return session;
        }

        public void setSession(Session session) {
            this.session = session;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @JsonView(UserView.class)
    public Output postLogin(@RequestBody Input input, HttpServletRequest request, HttpServletResponse response) {
        User user = authSvc.getUser(input.getUsername(), input.getPassword());

        if (user == null)
            throw new UnauthorizedException();

        return new Output(post(user, request, response));
    }

    private Session post(User user, HttpServletRequest request, HttpServletResponse response) {
        Session session = authSvc.createSession(user, getIp(request));

        Cookie cookie = new Cookie(sessionTokenInterceptor.getCookieName(), session.getToken());
        cookie.setHttpOnly(sessionTokenInterceptor.isCookieHttpOnly());
        cookie.setPath(sessionTokenInterceptor.getCookiePath());
        cookie.setMaxAge(session.getExpirySeconds());
        response.addCookie(cookie);

        return session;
    }

    private String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }
    
    @RequestMapping(value = "/fromContext", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @JsonView(UserView.class)
    public Output postFromContext(@AuthUserInj User user, HttpServletRequest request, HttpServletResponse response) {
        return new Output(post(user, request, response));
    }

}
