package it.mgt.atlas.repository.impl;

import it.mgt.atlas.entity.Role;
import it.mgt.atlas.repository.RoleRepo;
import it.mgt.util.spring.repository.BaseRepositoryImpl;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepoImpl extends BaseRepositoryImpl<Role, Long> implements RoleRepo {

    public RoleRepoImpl() {
        super(Role.class);
    }

    @Override
    public Long getKey(Role entity) {
        return entity.getId();
    }

    @Override
    public void setKey(Role entity, Long key) {
        entity.setId(key);
    }


    @Override
    public Role findByName(String name) {
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class);
        query.setParameter("name", name);
        return query.getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}