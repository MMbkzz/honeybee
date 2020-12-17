package com.stackstech.honeybee.server.bees.event;

public abstract class AbstractEvent<T> implements BeesEvent<T> {
    private T source;
    private EventType type;
    private EventSourceType sourceType;
    private EventPointcutType pointcutType;

    public AbstractEvent(T source,
                         EventType type,
                         EventSourceType sourceType,
                         EventPointcutType pointcutType) {
        this.source = source;
        this.type = type;
        this.sourceType = sourceType;
        this.pointcutType = pointcutType;
    }

    @Override
    public EventType getType() {
        return this.type;
    }

    @Override
    public EventPointcutType getPointcut() {
        return pointcutType;
    }

    @Override
    public EventSourceType getSourceType() {
        return sourceType;
    }

    @Override
    public T getSource() {
        return source;
    }
}
