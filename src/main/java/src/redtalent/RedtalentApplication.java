package src.redtalent;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import src.redtalent.security.Role;
import src.redtalent.repositories.RoleRepository;

@SpringBootApplication
public class RedtalentApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedtalentApplication.class, args);



    }
}
