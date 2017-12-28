package it.mgt.atlas.repository;

import it.mgt.atlas.entity.User;
import it.mgt.util.spring.repository.BaseRepository;

public interface UserRepo extends BaseRepository<User, Long> {

    User findByName(String name);

    User findByNameAndPassword(String name, String password);

    Number countByEmail(String email);

    Number countByName(String name);

    User findByEmail(String email);

}
