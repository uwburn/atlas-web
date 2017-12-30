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
    public User findByUsername(String username) {
        return em.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByNameAndPassword(String username, String password) {
        return em.createNamedQuery("User.findByUsernameAndPassword", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Number countByEmail(String email) {
        return em.createNamedQuery("User.countByEmail", Number.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public Number countByUsername(String username) {
        return em.createNamedQuery("User.countByUsername", Number.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public User findByEmail(String email) {
        return em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
