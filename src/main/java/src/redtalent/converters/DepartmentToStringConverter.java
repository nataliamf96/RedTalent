package src.redtalent.converters;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Department;
import src.redtalent.domain.Grade;

@Component
@Transactional
@Configuration
public class DepartmentToStringConverter implements Converter<Department, String>{
    public String convert(Department department) {
        String result;

        if (department == null)
            result = null;
        else
            result = String.valueOf(department.getId());

        return result;
    }
}
