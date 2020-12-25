package com.stackstech.honeybee.server.bees.event;

import com.stackstech.honeybee.server.bees.exception.BeesException;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "BeesJobEventHook")
public class JobEventHook implements BeesHook {
    @Override
    public void onEvent(BeesEvent event) throws BeesException {
        // This method needs to be reimplemented by event-consuming purpose
    }
}
