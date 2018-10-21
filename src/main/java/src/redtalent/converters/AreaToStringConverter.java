package src.redtalent.converters;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Area;
import src.redtalent.domain.Project;

@Component
@Transactional
@Configuration
public class AreaToStringConverter implements Converter<Area, String>{
    public String convert(Area area) {
        String result;

        if (area == null)
            result = null;
        else
            result = String.valueOf(area.getId());

        return result;
    }
}
