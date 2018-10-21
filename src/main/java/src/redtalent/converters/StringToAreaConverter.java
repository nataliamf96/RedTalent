package src.redtalent.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import src.redtalent.domain.Area;
import src.redtalent.domain.Department;
import src.redtalent.repositories.AreaRepository;
import src.redtalent.repositories.DepartmentRepository;

import java.util.Optional;

@Component
@Transactional
@Configuration
public class StringToAreaConverter implements Converter<String, Area>{

    @Autowired
    AreaRepository areaRepository;

    public Area convert(String text) {
        Optional<Area> result = null;
        try {
            if (StringUtils.isEmpty(text)) {
                result = null;
            } else {
                result = areaRepository.findById(text);
            }
        } catch (Exception oops) {
            throw new IllegalArgumentException(oops);
        }
        return result.get();
    }
}
