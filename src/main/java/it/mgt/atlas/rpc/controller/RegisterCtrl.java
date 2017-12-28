package it.mgt.atlas.rpc.controller;

import it.mgt.atlas.entity.Role;
import it.mgt.atlas.entity.User;
import it.mgt.atlas.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.mgt.atlas.service.UserSvc;
import it.mgt.util.spring.web.auth.AuthUserInj;
import it.mgt.util.spring.web.exception.ForbiddenException;
import java.util.Collections;

@Controller
@RequestMapping("/register")
public class RegisterCtrl {

    @Autowired
    UserSvc userService;
    
    @Autowired
    RoleRepo roleRepo;
    
    public static class Input {
        
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
    }
    
    public static class Output {
        
        private User user;

        public Output(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
        
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Output post(@AuthUserInj(required = false) User loggedUser,
            @RequestBody Input input) {
        if (loggedUser != null)
            throw new ForbiddenException();

        Role userRole = roleRepo.findByName("user");

        User user = new User(input.getUsername(), input.getPassword(), 
                input.getEmail(), input.getFirstName(), input.getLastName(), 
                Collections.singleton(userRole));

        userService.createUser(user);

        return new Output(user);
    }

}
