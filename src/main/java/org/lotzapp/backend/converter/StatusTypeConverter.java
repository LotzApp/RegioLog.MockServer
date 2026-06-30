package org.lotzapp.backend.converter;

import org.lotzapp.adminapi.model.StatusType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StatusTypeConverter implements Converter<String, StatusType> {
    @Override
    public StatusType convert(String source) {
        return StatusType.valueOf(source.toUpperCase());
    }
}
