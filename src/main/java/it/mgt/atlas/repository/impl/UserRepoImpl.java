package it.mgt.atlas.repository.impl;

import it.mgt.atlas.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import it.mgt.atlas.repository.UserRepo;
import it.mgt.util.spring.repository.BaseRepositoryImpl;

@Repository
public class UserRepoImpl extends BaseRepositoryImpl<User, Long> implements UserRepo {

    public UserRepoImpl() {
        super(User.class);
    }

    @Override
    public Long getKey(User entity) {
        return  entity.getId();
    }

    @Override
    public void setKey(User user, Long key) {
        user.setId(key);
    }

    @Override
    public User findByName(String username) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :username");
        query.setParameter("username", username);
        List<User> list = query.getResultList();
        if (list.isEmpty())
            return null;

        return list.get(0);
    }

    @Override
    public User findByNameAndPassword(String username, String password) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<User> list = query.getResultList();
        if (list.isEmpty())
            return null;

        return list.get(0);
    }

    @Override
    public Number countByEmail(String email) {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email");
        query.setParameter("email", email);

        return (Number) query.getSingleResult();
    }

    @Override
    public Number countByName(String username) {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username");
        query.setParameter("username", username);

        return (Number) query.getSingleResult();
    }

    @Override
    public User findByEmail(String email) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        List<User> list = query.getResultList();
        if (list.isEmpty())
            return null;

        return list.get(0);
    }

}
