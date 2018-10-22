package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Comment;
import src.redtalent.domain.Reply;
import src.redtalent.repositories.CommentRepository;
import src.redtalent.repositories.ReplyRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    public ReplyService() {
        super();
    }

    public Reply create(){
        Reply reply;

        reply = new Reply();
        Date date = new Date(System.currentTimeMillis() - 1000);
        reply.setMoment(date);

        return reply;
    }

    public Reply findOne(String replyId) {
        Assert.notNull(replyId, "Reply Service : id null");
        Optional<Reply> result = replyRepository.findById(replyId);
        return result.get();
    }

    public List<Reply> findAll() {
        List<Reply> result = replyRepository.findAll();
        Assert.notNull(result, "Reply Service : list null");
        return result;
    }

    public Reply save(Reply reply) {
        Assert.notNull(reply, "Reply Service : Objeto null");
        Reply result = replyRepository.save(reply);
        return result;
    }

    public void remove(Reply reply) {
        Assert.notNull(reply, "Reply Service : Objeto null");
        replyRepository.delete(reply);
    }
}
