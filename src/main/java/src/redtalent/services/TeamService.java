package src.redtalent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import src.redtalent.domain.Team;
import src.redtalent.repositories.TeamRepository;

import java.util.Collection;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public TeamService(){
        super();
    }

    public Team findOne(String teamId){
        Assert.notNull(teamId);
        Team result = teamRepository.findOne(teamId);
        Assert.notNull(result);
        return result;
    }

    public Collection<Team> findAll(){
        Collection<Team> result = teamRepository.findAll();
        Assert.notNull(result);
        return result;
    }

    public Team save(Team team){
        Assert.notNull(team);
        Team result = teamRepository.save(team);
        return result;
    }

    public void remove(Team team){
        Assert.notNull(team);
        teamRepository.delete(team);
    }
}
