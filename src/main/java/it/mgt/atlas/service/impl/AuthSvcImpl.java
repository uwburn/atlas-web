package it.mgt.atlas.service.impl;

import it.mgt.atlas.entity.Session;
import it.mgt.atlas.entity.User;
import it.mgt.atlas.repository.SessionRepo;
import it.mgt.atlas.repository.UserRepo;
import it.mgt.atlas.service.AuthSvc;
import it.mgt.util.spring.auth.AuthSession;
import it.mgt.util.spring.auth.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthSvcImpl implements AuthSvc {

    Logger logger = LoggerFactory.getLogger(AuthSvcImpl.class);

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private SessionRepo sessionRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${it.mgt.atlas.sessionIdLength}")
    private int sessionIdLength;

    @Value("${it.mgt.atlas.sessionDuration}")
    private long sessionDuration;

    @Override
    @Transactional
    public AuthUser getAuthUser(String name, String password) {
        return getUser(name, password);
    }

    @Override
    @Transactional
    public AuthUser getAuthUser(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    @Transactional
    public AuthSession getValidAuthSession(String token) {
        return sessionRepository.findActiveByToken(token, new Date());
    }

    @Override
    public String generateToken() {
        Date now = new Date();
        String token;
        do {
            token = randomToken();
        } while(sessionRepository.findActiveByToken(token, now) != null);

        return token;
    }

    @Override
    @Transactional
    public void invalidateSession(AuthSession authSession) {
        Session session = (Session) authSession;
        session.setEndDate(new Date());
    }

    private String randomToken() {
        byte[] rnd = new byte[sessionIdLength];
        secureRandom.nextBytes(rnd);
        return Base64.getEncoder().encodeToString(rnd);
    }

    @Override
    public String hashPassword(String password) {
        if (password == null)
            return null;

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(password.getBytes());
            byte[] sha = sha256.digest();
            return Base64.getEncoder().encodeToString(sha);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to hash password", e);
            return null;
        }
    }

    @Override
    @Transactional
    public User getUser(String name, String password) {
        String hashedPassword = hashPassword(password);
        return userRepository.findByNameAndPassword(name, hashedPassword);
    }

    @Override
    public Session createSession(User user, String ip) {
        String token = generateToken();
        Session session = new Session(user, token, ip, new Date(new Date().getTime() + sessionDuration));
        sessionRepository.persist(session);

        return session;
    }

    @Override
    @Transactional
    public void touchSession(AuthSession authSession) {        
        Session session = (Session) authSession;
        session.setEndDate(new Date(new Date().getTime() + sessionDuration));
    }
}
