package it.mgt.atlas.repository.impl;

import it.mgt.atlas.entity.Session;
import it.mgt.atlas.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;
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
        Query query = em.createQuery("SELECT s FROM Session s WHERE s.endDate >= :now and s.token = :token");
        query.setParameter("now", now);
        query.setParameter("token", token);
        List<Session> list = query.getResultList();
        if (list.isEmpty())
            return null;

        return list.get(0);
    }

    @Override
    public Number countActiveByUser(User user, Date now) {
        Query query = em.createQuery("SELECT COUNT(s) FROM Session s WHERE s.endDate >= :now AND s.user = :user");
        query.setParameter("now", now);
        query.setParameter("user", user);
        return (Number) query.getSingleResult();
    }

}
