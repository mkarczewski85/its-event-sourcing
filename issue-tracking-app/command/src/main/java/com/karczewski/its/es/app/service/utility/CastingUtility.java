package com.karczewski.its.es.app.service.utility;

public final class CastingUtility {

    private CastingUtility() {
    }

    public static <T> T safeCast(Object obj, Class<T> clazz) {
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        throw new ClassCastException("Expected " + clazz.getName() +
                " but got: " + (obj == null ? "null" : obj.getClass().getName()));
    }

}
