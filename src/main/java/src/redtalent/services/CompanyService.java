package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Company;
import src.redtalent.repositories.CompanyRepository;

import java.util.Collection;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyService(){
        super();
    }

    public Company findOne(String companyId){
        Assert.notNull(companyId);
        return companyRepository.findOne(companyId);
    }

    public Collection<Company> findAll(){
        Collection<Company> result = companyRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public Company save(Company company){
        Assert.notNull(company);
        company.setSuspicious(false);
        Company result = companyRepository.save(company);
        return result;
    }

    public void remove(Company company){
        Assert.notNull(company);
        companyRepository.delete(company);
    }

}
