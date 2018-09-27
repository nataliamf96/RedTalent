package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.repositories.StudentRepository;

import java.util.List;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentService(){
        super();
    }

    public Student findOne(int actorId) {
        Student r;
        r = studentRepository.findOne(actorId);
        Assert.notNull(r);

        return r;
    }

    public List<Student> findAllStudents(){
        return studentRepository.findAll();
    }

}
