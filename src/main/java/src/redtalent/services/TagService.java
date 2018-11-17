package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Tag;
import src.redtalent.repositories.TagRepository;

import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagService(){
        super();
    }

    public Tag create(){
        return new Tag();
    }

    public Collection<Tag> findAll(){
        return tagRepository.findAll();
    }

    public Tag findOne(String tagId) {
        Assert.notNull(tagId, "Tag Service : id null");
        Optional<Tag> result = tagRepository.findById(tagId);
        return result.get();
    }

    public Tag save(Tag tag) {
        Assert.notNull(tag, "Tag Service : Objeto null");
        Tag result = tagRepository.save(tag);
        return result;
    }

    public void remove(Tag tag) {
        Assert.notNull(tag, "Tag Service : Objeto null");
        tagRepository.delete(tag);
    }

}
