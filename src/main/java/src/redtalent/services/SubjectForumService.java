package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.SubjectForum;
import src.redtalent.repositories.SubjectForumRepository;

import javax.security.auth.Subject;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubjectForumService {

    @Autowired
    private SubjectForumRepository subjectForumRepository;

    public SubjectForumService() {
        super();
    }

    public SubjectForum create(){
        SubjectForum subjectForum;

        subjectForum = new SubjectForum();
        Date date = new Date(System.currentTimeMillis() - 1000);
        subjectForum.setMoment(date);

        return subjectForum;
    }

    public SubjectForum findOne(String subjectForumId) {
        Assert.notNull(subjectForumId, "Forum Service : id null");
        Optional<SubjectForum> result = subjectForumRepository.findById(subjectForumId);
        return result.get();
    }

    public List<SubjectForum> findAll() {
        List<SubjectForum> result = subjectForumRepository.findAll();
        Assert.notNull(result, "Forum Service : list null");
        return result;
    }

    public SubjectForum save(SubjectForum subjectForum) {
        Assert.notNull(subjectForum, "Forum Service : Objeto null");
        SubjectForum result = subjectForumRepository.save(subjectForum);
        return result;
    }

    public void remove(SubjectForum subjectForum) {
        Assert.notNull(subjectForum, "Forum Service : Objeto null");
        subjectForumRepository.delete(subjectForum);
    }
}