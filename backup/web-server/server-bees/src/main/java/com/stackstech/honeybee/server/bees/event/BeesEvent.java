package com.stackstech.honeybee.server.bees.event;

/**
 * A semantic event which indicates that a griffin-defined action occurred.
 * This high-level event is generated by an action (such as an
 * <code>addJob</code>) when the task-specific action occurs.
 * The event is passed to every <code>GriffinHook</code> object
 * that registered to receive such events using configuration.
 *
 * @author Eugene Liu
 * @since 0.3
 */
public interface BeesEvent<T> {
    /**
     * @return concrete event type
     */
    EventType getType();

    /**
     * @return concrete event pointcut type
     */
    EventPointcutType getPointcut();

    /**
     * @return concrete event source type
     */
    EventSourceType getSourceType();

    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    T getSource();
}