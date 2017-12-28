package it.mgt.atlas.repository;

import it.mgt.atlas.entity.Role;
import it.mgt.util.spring.repository.BaseRepository;

public interface RoleRepo extends BaseRepository<Role, Long> {

    Role findByName(String name);
}
