package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Blog;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Forum;
import src.redtalent.repositories.ForumRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    public ForumService(){
        super();
    }

    public Forum create(){
        Forum forum;

        forum = new Forum();
        Date date = new Date(System.currentTimeMillis() - 1000);
        forum.setMoment(date);

        return forum;
    }

    public Forum findOne(String forumId){
        Assert.notNull(forumId,"Forum Service : id null");
        Optional<Forum> result = forumRepository.findById(forumId);
        return result.get();
    }

    public List<Forum> findAll(){
        List<Forum> result = forumRepository.findAll();
        Assert.notNull(result,"Forum Service : list null");
        return result;
    }

    public Forum save(Forum forum){
        Assert.notNull(forum,"Forum Service : Objeto null");
        Forum result = forumRepository.save(forum);
        return result;
    }

    public void remove(Forum forum){
        Assert.notNull(forum,"Forum Service : Objeto null");
        forumRepository.delete(forum);
    }

    public Forum findForumByCommentsContaining(Comment comment){
        Assert.notNull(comment, "El comentario es nulo");
        Forum result = forumRepository.findForumByCommentsContaining(comment);
        return result;
    }
}
