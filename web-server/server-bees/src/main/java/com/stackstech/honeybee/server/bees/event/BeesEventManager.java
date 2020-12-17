package com.stackstech.honeybee.server.bees.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class BeesEventManager {
    @Autowired
    private ApplicationContext applicationContext;

    @Value("#{'${internal.event.listeners}'.split(',')}")
    private Set<String> enabledListeners;

    private List<BeesHook> eventListeners;

    @PostConstruct
    void initializeListeners() {
        List<BeesHook> eventListeners = new ArrayList<>();
        applicationContext.getBeansOfType(BeesHook.class)
                .forEach((beanName, listener) -> {
                    if (enabledListeners.contains(beanName)) {
                        eventListeners.add(listener);
                    }
                });
        this.eventListeners = eventListeners;
    }

    public void notifyListeners(BeesEvent event) {
        eventListeners.forEach(listener -> {
            listener.onEvent(event);
        });
    }
}
