package it.mgt.atlas.service;

import it.mgt.atlas.entity.Session;
import it.mgt.atlas.entity.User;

public interface AuthSvc extends it.mgt.util.spring.auth.AuthSvc {

    User getUser(String name, String password);

    Session createSession(User user, String ip);
    
}
