package it.mgt.atlas.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import it.mgt.atlas.entity.Operation;
import it.mgt.atlas.entity.User;
import it.mgt.atlas.service.AuthSvc;
import it.mgt.atlas.service.UserSvc;
import it.mgt.atlas.view.DefaultView;

import it.mgt.util.spring.web.auth.AuthUserInj;
import it.mgt.util.spring.web.auth.RequiredOperation;

@RequestMapping("/users")
@Controller
public class UsersCtrl {

    @Autowired
    AuthSvc authSvc;

    @Autowired
    UserSvc userService;

    @RequestMapping(method = RequestMethod.GET, value = "/self")
    @ResponseBody
    @RequiredOperation(Operation.READ_PROFILE)
    @JsonView(DefaultView.class)
    public User getSelf(@AuthUserInj User user) {
        return user;
    }

}
