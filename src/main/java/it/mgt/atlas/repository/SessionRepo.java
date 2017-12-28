package it.mgt.atlas.repository;

import it.mgt.atlas.entity.Session;
import it.mgt.atlas.entity.User;
import it.mgt.util.spring.repository.BaseRepository;

import java.util.Date;

public interface SessionRepo extends BaseRepository<Session, Long> {

    Session findActiveByToken(String token, Date now);

    Number countActiveByUser(User user, Date now);

}
