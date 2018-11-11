package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Blog;
import src.redtalent.domain.Comment;
import src.redtalent.repositories.BlogRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public BlogService() {
        super();
    }

    public Blog create(){
        Blog blog;

        blog = new Blog();
        Date date = new Date(System.currentTimeMillis() - 1000);
        blog.setMoment(date);

        return blog;
    }

    public Blog findOne(String blogId) {
        Assert.notNull(blogId, "Blog Service : id null");
        Optional<Blog> result = blogRepository.findById(blogId);
        return result.get();
    }

    public List<Blog> findAll() {
        List<Blog> result = blogRepository.findAll();
        Assert.notNull(result, "Blog Service : list null");
        return result;
    }

    public Blog save(Blog blog) {
        Assert.notNull(blog, "Blog Service : Objeto null");
        Blog result = blogRepository.save(blog);
        return result;
    }

    public void remove(Blog blog) {
        Assert.notNull(blog, "Blog Service : Objeto null");
        blogRepository.delete(blog);
    }

    public Blog findBlogByCommentsContaining(Comment comment){
        Assert.notNull(comment, "El comentario es nulo");
        Blog result = blogRepository.findBlogByCommentsContaining(comment);
        return result;
    }
}