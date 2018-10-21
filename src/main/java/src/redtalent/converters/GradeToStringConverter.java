package src.redtalent.converters;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Area;
import src.redtalent.domain.Grade;

@Component
@Transactional
@Configuration
public class GradeToStringConverter implements Converter<Grade, String>{
    public String convert(Grade grade) {
        String result;

        if (grade == null)
            result = null;
        else
            result = String.valueOf(grade.getId());

        return result;
    }
}
