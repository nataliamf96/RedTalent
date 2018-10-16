package src.redtalent.converters;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Project;

@Component
@Transactional
@Configuration
public class ProjectToStringConverter implements Converter<Project, String>{
    public String convert(Project project) {
        String result;

        if (project == null)
            result = null;
        else
            result = String.valueOf(project.getId());

        return result;
    }
}
