package it.mgt.atlas.rpc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import it.mgt.atlas.entity.Operation;
import it.mgt.atlas.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import it.mgt.atlas.service.UserSvc;
import it.mgt.atlas.view.DefaultView;
import it.mgt.util.spring.auth.AuthSvc;
import it.mgt.util.spring.web.auth.AuthUserInj;
import it.mgt.util.spring.web.auth.RequiredOperation;
import it.mgt.util.spring.web.exception.ForbiddenException;

@Controller
@RequestMapping("/updateSelf")
public class UpdateSelf {

    @Autowired
    UserSvc userService;
    
    @Autowired
    AuthSvc authSvc;
    
    public static class Input {
        
        private String currentPassword;
        private String newPassword;
        private String email;
        private String firstName;
        private String lastName;

        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
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
    @RequiredOperation(Operation.MODIFY_PROFILE)
    @JsonView(DefaultView.class)
    public Output post(@AuthUserInj User user, @RequestBody Input input) {
        String hashedPassword = authSvc.hashPassword(input.getCurrentPassword());
        
        if (!user.getPassword().equals(hashedPassword))
            throw new ForbiddenException();
        
        if (input.getNewPassword() != null)
            user.setPassword(input.getNewPassword());
        else
            user.setPassword(input.getCurrentPassword());
        if (input.getEmail() != null)
            user.setEmail(input.getEmail());
        if (input.getFirstName() != null)
            user.setFirstName(input.getFirstName());
        if (input.getLastName() != null)
            user.setLastName(input.getLastName());

        userService.updateUser(user);

        return new Output(user);
    }

}
