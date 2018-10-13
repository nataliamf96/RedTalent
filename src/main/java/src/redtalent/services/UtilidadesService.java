package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.redtalent.domain.Administrator;
import src.redtalent.repositories.AdministratorRepository;
import src.redtalent.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilidadesService {

    @Autowired
    private UserService userService;
    @Autowired
    private AdministratorService administratorService;

    public UtilidadesService(){
        super();
    }

    public List<String> allEmails(){
        List<String> result = new ArrayList<String>();
        List<String> a = userService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        List<String> b = administratorService.findAll().stream().map(z->z.getEmail()).collect(Collectors.toList());
        return result;
    }

}
