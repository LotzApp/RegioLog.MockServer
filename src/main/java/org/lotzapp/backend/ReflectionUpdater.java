package org.lotzapp.backend;

import org.jspecify.annotations.NonNull;
import org.openapitools.jackson.nullable.JsonNullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReflectionUpdater {

  private static final List<String> IGNORE_FIELDS = Arrays.asList("id", "lastUpdate", "createdAt");

  /**
   * Copies all non-null fields from {@code source} into {@code target}. Skips the "id" field to
   * prevent overwriting the primary key.
   */
  public static <RestEntity, DatabaseEntity> void updateFields(
      RestEntity target, DatabaseEntity source) {
    Class<?> clazzTarget = target.getClass();
    Class<?> clazzSource = source.getClass();

    var sourceFields = Arrays.asList(clazzSource.getDeclaredFields());
    while (clazzTarget != null) {
      for (var field : clazzTarget.getDeclaredFields()) {
        if (IGNORE_FIELDS.contains(field.getName())) continue;
        var fieldName = field.getName();
        var sourceField = getFieldsByName(fieldName, sourceFields);

        if (sourceField.isPresent()) {
          try {
            sourceField.get().setAccessible(true);
            var jsonNullable = sourceField.get().getType().equals(JsonNullable.class);
            var sourceObject = jsonNullable ? ((JsonNullable<?>) sourceField.get().get(source)) : sourceField.get().get(source);
            var valuePresent = jsonNullable ? ((JsonNullable<?>) sourceObject).isPresent() : sourceObject != null;

            field.setAccessible(true);
            if(!valuePresent) {
              field.set(target, null);
            } else {
              var sourceType = jsonNullable ? ((JsonNullable<?>) sourceObject).get().getClass() : sourceField.get().getType();
              var sourceValue = jsonNullable ? ((JsonNullable<?>) sourceObject).get() : sourceObject;
              if (sourceType.equals(field.getType())) {
                field.set(target, sourceValue);
              }
            }

          } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update field: " + field.getName(), e);
          }
        }
      }
      clazzTarget = clazzTarget.getSuperclass();
    }
  }

  private static @NonNull Optional<Field> getFieldsByName(String name, List<Field> sourceFields) {
    return sourceFields.stream().filter(f -> f.getName().equals(name)).findFirst();
  }
}
