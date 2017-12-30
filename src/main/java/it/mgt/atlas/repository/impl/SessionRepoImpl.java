package it.mgt.atlas.repository.impl;

import it.mgt.atlas.entity.Session;
import it.mgt.atlas.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
import it.mgt.atlas.repository.SessionRepo;
import it.mgt.util.spring.repository.BaseRepositoryImpl;

@Repository
public class SessionRepoImpl extends BaseRepositoryImpl<Session, Long> implements SessionRepo {

    public SessionRepoImpl() {
        super(Session.class);
    }

    @Override
    public Long getKey(Session entity) {
        return entity.getId();
    }

    @Override
    public void setKey(Session session, Long key) {
        session.setId(key);
    }

    @Override
    public Session findActiveByToken(String token, Date now) {
        return em.createNamedQuery("Session.findActiveByToken", Session.class)
                .setParameter("now", now)
                .setParameter("token", token)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Number countActiveByUser(User user, Date now) {
        return em.createNamedQuery("Session.countActiveByUser", Number.class)
                .setParameter("now", now)
                .setParameter("user", user)
                .getSingleResult();
    }

}
