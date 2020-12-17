package com.stackstech.honeybee.server.bees.event;

import com.stackstech.honeybee.server.bees.exception.BeesException;

/**
 * The Hook interface for receiving internal events.
 * The class that is interested in processing an event
 * implements this interface, and the object created with that
 * class is registered to griffin, using the configuration.
 * When the event occurs, that object's <code>onEvent</code> method is
 * invoked.
 *
 * @author Eugene Liu
 * @since 0.3
 */
public interface BeesHook {
    /**
     * Invoked when an action occurs.
     *
     * @see BeesEvent
     */
    void onEvent(BeesEvent event) throws BeesException;
}
