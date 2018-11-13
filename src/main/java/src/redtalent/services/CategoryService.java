package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Category;
import src.redtalent.repositories.CategoryRepository;

import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryService(){
        super();
    }

    public Category create(){
        return new Category();
    }

    public Collection<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findOne(String categoryId) {
        Assert.notNull(categoryId, "Category Service : id null");
        Optional<Category> result = categoryRepository.findById(categoryId);
        return result.get();
    }

    public Category save(Category category) {
        Assert.notNull(category, "Category Service : Objeto null");
        Category result = categoryRepository.save(category);
        return result;
    }

    public void remove(Category category) {
        Assert.notNull(category, "Category Service : Objeto null");
        categoryRepository.delete(category);
    }

}
