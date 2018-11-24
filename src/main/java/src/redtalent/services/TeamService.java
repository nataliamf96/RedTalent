package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.*;
import src.redtalent.repositories.TeamRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public TeamService(){
        super();
    }

    public Team create(){
        Team team = new Team();
        return team;
    }
    public Team findOne(String teamId){
        Assert.notNull(teamId,"Team Service : id null");
        Optional<Team> result = teamRepository.findById(teamId);
        return result.get();
    }

    public Collection<Team> findAll(){
        Collection<Team> result = teamRepository.findAll();
        Assert.notNull(result,"Team Service : list null");
        return result;
    }

    public Team save(Team team){
        Assert.notNull(team,"Team Service : Objeto null");
        Team result = teamRepository.save(team);
        return result;
    }

    public void remove(Team team){
        Assert.notNull(team, "Team Service : Objeto null");
        teamRepository.delete(team);
    }

    public Team teamByProjectId(Project project){
        Assert.notNull(project,"Team Service : Objeto null");
        Team result = teamRepository.findTeamByProjectsContaining(project);
        return result;
    }

    public Team findTeamByApplicationsContaining(Application application){
        Assert.notNull(application,"La solicitud es NULL");
        Team result = teamRepository.findTeamByApplicationsContaining(application);
        return result;
    }

    public Team findTeamByCommentsContains(Comment comment){
        return teamRepository.findTeamByCommentsContains(comment);
    }
}