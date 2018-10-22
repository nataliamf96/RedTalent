package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Comment;
import src.redtalent.domain.SubjectForum;
import src.redtalent.repositories.CommentRepository;
import src.redtalent.repositories.SubjectForumRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentService() {
        super();
    }

    public Comment create(){
        Comment comment;

        comment = new Comment();
        Date date = new Date(System.currentTimeMillis() - 1000);
        comment.setMoment(date);

        return comment;
    }

    public Comment findOne(String commentId) {
        Assert.notNull(commentId, "Comment Service : id null");
        Optional<Comment> result = commentRepository.findById(commentId);
        return result.get();
    }

    public List<Comment> findAll() {
        List<Comment> result = commentRepository.findAll();
        Assert.notNull(result, "Comment Service : list null");
        return result;
    }

    public Comment save(Comment comment) {
        Assert.notNull(comment, "Comment Service : Objeto null");
        Comment result = commentRepository.save(comment);
        return result;
    }

    public void remove(Comment comment) {
        Assert.notNull(comment, "Comment Service : Objeto null");
        commentRepository.delete(comment);
    }
}
