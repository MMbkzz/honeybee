package com.stackstech.honeybee.server.quality.factory;

import com.stackstech.honeybee.server.core.exception.BeesException;
import com.stackstech.honeybee.server.quality.entity.SegmentPredicate;
import com.stackstech.honeybee.server.quality.service.Predicator;
import com.stackstech.honeybee.server.quality.service.impl.FileExistPredicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.stackstech.honeybee.server.core.exception.BeesExceptionMessage.PREDICATE_TYPE_NOT_FOUND;

public class PredicatorFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PredicatorFactory.class);

    public static Predicator newPredicateInstance(SegmentPredicate segPredicate) {
        Predicator predicate;
        switch (segPredicate.getType()) {
            case "file.exist":
                predicate = new FileExistPredicator(segPredicate);
                break;
            case "custom":
                predicate = getPredicateBean(segPredicate);
                break;
            default:
                throw new BeesException.NotFoundException(PREDICATE_TYPE_NOT_FOUND);
        }
        return predicate;
    }

    private static Predicator getPredicateBean(SegmentPredicate segmentPredicate) {
        Predicator predicate;
        String predicateClassName = (String) segmentPredicate.getConfigMap().get("class");
        try {
            Class clazz = Class.forName(predicateClassName);
            Constructor<Predicator> constructor = clazz.getConstructor(SegmentPredicate.class);
            predicate = constructor.newInstance(segmentPredicate);
        } catch (ClassNotFoundException e) {
            String message = "There is no predicate type that you input.";
            LOGGER.error(message, e);
            throw new BeesException.ServiceException(message, e);
        } catch (NoSuchMethodException e) {
            String message = "For predicate with type " + predicateClassName +
                    " constructor with parameter of type " + SegmentPredicate.class.getName() + " not found";
            LOGGER.error(message, e);
            throw new BeesException.ServiceException(message, e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            String message = "Error creating predicate bean";
            LOGGER.error(message, e);
            throw new BeesException.ServiceException(message, e);
        }
        return predicate;
    }
}
