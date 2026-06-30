package org.lotzapp.backend.converter;

import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Interface for converting between RestEntity and DatabaseEntity
 * @param <RestEntity> Rest entity to convert to
 * @param <DatabaseEntity> Target database entity
 */
public interface IConverter<RestEntity, DatabaseEntity> {
    /**
     * Convert a DatabaseEntity to a RestEntity
     * @param entity target entity to convert
     * @return converted entity
     */
    default RestEntity toRest(DatabaseEntity entity) {
        throw new UnsupportedOperationException("Cannot convert to RestEntity");
    }

    /**
     * Convert a RestEntity to a DatabaseEntity
     * @param entity target entity to convert
     * @return converted entity
     */
    default DatabaseEntity toEntity(RestEntity entity) {
        throw new UnsupportedOperationException("Cannot convert to DatabaseEntity");
    }

    /**
     * Convert a collection of DatabaseEntity to a collection of RestEntity
     * @param entities entities to convert
     * @return converted entities
     */
    default List<RestEntity> toRest(Collection<DatabaseEntity> entities) {
        return entities.stream().map(this::toRest).toList();
    }

    /**
     * Convert a collection of RestEntity to a collection of DatabaseEntity
     * @param entities entities to convert
     * @return converted entities
     */
    default List<DatabaseEntity> toEntity(Collection<RestEntity> entities) {
        return entities.stream().map(this::toEntity).toList();
    }
}
