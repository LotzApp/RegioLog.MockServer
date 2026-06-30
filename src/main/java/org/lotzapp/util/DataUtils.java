package org.lotzapp.util;

import lombok.experimental.UtilityClass;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.function.Consumer;

@UtilityClass
public class DataUtils {
    public static <T> void addIfPresent(JsonNullable<T> value, Consumer<T> applier) {
        if (value.isPresent()) {
            applier.accept(value.get());
        }
    }

    public static <T> void updateIfPresent(JsonNullable<T> value, Consumer<T> applier, T defaultValue) {
        if (value.isPresent()) {
            applier.accept(value.get());
        } else {
            applier.accept(defaultValue);
        }
    }

    public static <T> void addIfPresent(T value, Consumer<T> applier) {
        if (value != null) {
            applier.accept(value);
        }
    }

    public static <T> void updateIfPresent(T value, Consumer<T> applier, T defaultValue) {
        if (value != null) {
            applier.accept(value);
        } else applier.accept(defaultValue);
    }
}
