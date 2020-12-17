package com.stackstech.honeybee.server.bees.event;


import com.stackstech.honeybee.server.bees.entity.AbstractJob;

public class JobEvent extends AbstractEvent<AbstractJob> {

    private JobEvent(AbstractJob source,
                     EventType type,
                     EventSourceType sourceType,
                     EventPointcutType pointcutType) {
        super(source, type, sourceType, pointcutType);
    }

    public static JobEvent yieldJobEventBeforeCreation(AbstractJob source) {
        return new JobEvent(source,
                EventType.CREATION_EVENT,
                EventSourceType.JOB,
                EventPointcutType.BEFORE);
    }

    public static JobEvent yieldJobEventAfterCreation(AbstractJob source) {
        return new JobEvent(source,
                EventType.CREATION_EVENT,
                EventSourceType.JOB,
                EventPointcutType.AFTER);
    }

    public static JobEvent yieldJobEventBeforeRemoval(AbstractJob source) {
        return new JobEvent(source,
                EventType.REMOVAL_EVENT,
                EventSourceType.JOB,
                EventPointcutType.BEFORE);
    }

    public static JobEvent yieldJobEventAfterRemoval(AbstractJob source) {
        return new JobEvent(source,
                EventType.REMOVAL_EVENT,
                EventSourceType.JOB,
                EventPointcutType.AFTER);
    }
}
