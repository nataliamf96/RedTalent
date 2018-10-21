package src.redtalent.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import src.redtalent.domain.Department;
import src.redtalent.domain.Project;
import src.redtalent.repositories.DepartmentRepository;
import src.redtalent.repositories.ProjectRepository;

import java.util.Optional;

@Component
@Transactional
@Configuration
public class StringToDepartmentConverter implements Converter<String, Department>{

    @Autowired
    DepartmentRepository departmentRepository;

    public Department convert(String text) {
        Optional<Department> result = null;
        try {
            if (StringUtils.isEmpty(text)) {
                result = null;
            } else {
                result = departmentRepository.findById(text);
            }
        } catch (Exception oops) {
            throw new IllegalArgumentException(oops);
        }
        return result.get();
    }
}
