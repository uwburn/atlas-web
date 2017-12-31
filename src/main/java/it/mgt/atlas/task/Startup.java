package it.mgt.atlas.task;

import it.mgt.atlas.entity.Example;
import it.mgt.atlas.entity.Operation;
import it.mgt.atlas.entity.Role;
import it.mgt.atlas.entity.User;
import it.mgt.atlas.repository.ExampleRepo;
import it.mgt.atlas.repository.RoleRepo;
import it.mgt.atlas.repository.UserRepo;
import it.mgt.atlas.service.AuthSvc;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Startup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

    private boolean fired = false;
    
    @Autowired
    private RoleRepo roleRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private ExampleRepo exampleRepo;
    
    @Autowired
    private AuthSvc authSvc;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (fired)
            return;

        fired = true;

        baseData();
    }
    
    private void baseData() {
        Role admin = roleRepo.findByName("admin");
        if (admin == null) {            
            LOGGER.debug("Creating admin role");
            
            admin = new Role("admin", Operation.values(), 0);
            roleRepo.persist(admin);
        }
        
        User atlas = userRepo.findByUsername("atlas");
        if (atlas == null) {
            LOGGER.debug("Creating atlas user");
            
            Set<Role> roles = new HashSet<>();
            roles.add(admin);
            
            String hashedPassword = authSvc.hashPassword("atlas");
            atlas = new User("atlas", hashedPassword, "atlas@mgt.it", "atlas", "atlas", roles);
            userRepo.persist(atlas);
        }
        
        Example ex1 = exampleRepo.findByCode("Ex1");
        if (ex1 == null) {
            LOGGER.debug("Creating Ex1 example");
            
            ex1 = new Example("Ex1", new Date());
            exampleRepo.persist(ex1);
        }
        
        Example ex2 = exampleRepo.findByCode("Ex2");
        if (ex2 == null) {
            LOGGER.debug("Creating Ex2 example");
            
            ex2 = new Example("Ex2", new Date());
            exampleRepo.persist(ex2);
        }
    }

}
