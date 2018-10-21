package src.redtalent.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import src.redtalent.domain.Area;
import src.redtalent.domain.Grade;
import src.redtalent.repositories.AreaRepository;
import src.redtalent.repositories.GradeRepository;

import java.util.Optional;

@Component
@Transactional
@Configuration
public class StringToGradeConverter implements Converter<String, Grade>{

    @Autowired
    GradeRepository gradeRepository;

    public Grade convert(String text) {
        Optional<Grade> result = null;
        try {
            if (StringUtils.isEmpty(text)) {
                result = null;
            } else {
                result = gradeRepository.findById(text);
            }
        } catch (Exception oops) {
            throw new IllegalArgumentException(oops);
        }
        return result.get();
    }
}
