package it.mgt.atlas.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Startup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

    private boolean fired = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (fired)
            return;

        fired = true;

        // Your code here
    }

}
