package com.stackstech.honeybee.server.bees.service;

import java.io.IOException;

/**
 * Predicator is an object that judges if one condition is met.
 * This interface only has one method {@link #predicate()}
 */
public interface Predicator {
    /**
     * predicate a condition
     *
     * @return True condition is met, otherwise False
     * @throws IOException
     */
    boolean predicate() throws IOException;
}
