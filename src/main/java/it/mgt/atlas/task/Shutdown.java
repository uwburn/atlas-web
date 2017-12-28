package it.mgt.atlas.task;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Shutdown implements ApplicationListener<ContextClosedEvent> {

    private boolean fired = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextClosedEvent event) {
        if (fired)
            return;

        fired = true;

        // Your code here
    }

}
