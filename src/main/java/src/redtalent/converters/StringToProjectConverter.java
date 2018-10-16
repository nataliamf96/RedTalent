package src.redtalent.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import src.redtalent.domain.Project;
import src.redtalent.repositories.ProjectRepository;

import java.util.Optional;

@Component
@Transactional
@Configuration
public class StringToProjectConverter implements Converter<String, Project>{

    @Autowired
    ProjectRepository projectRepository;

    public Project convert(String text) {
        Optional<Project> result = null;
        try {
            if (StringUtils.isEmpty(text)) {
                result = null;
            } else {
                result = projectRepository.findById(text);
            }
        } catch (Exception oops) {
            throw new IllegalArgumentException(oops);
        }
        return result.get();
    }
}
