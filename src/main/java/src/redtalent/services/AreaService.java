package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Area;
import src.redtalent.domain.Department;
import src.redtalent.repositories.AreaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public AreaService() {
        super();
    }

    public Area create(){
        Area area = new Area();
        return area;
    }

    public Area findOne(String areaId) {
        Assert.notNull(areaId, "Area Service : id null");
        Optional<Area> result = areaRepository.findById(areaId);
        return result.get();
    }

    public List<Area> findAll() {
        List<Area> result = areaRepository.findAll();
        Assert.notNull(result, "Area Service : list null");
        return result;
    }

    public Area save(Area area) {
        Assert.notNull(area, "Area Service : Objeto null");
        Area result = areaRepository.save(area);
        return result;
    }

    public void remove(Area area) {
        Assert.notNull(area, "Area Service : Objeto null");
        areaRepository.delete(area);
    }

    public Area findAreaByDepartamentsContaining(Department department){
        Assert.notNull(department, "El departamento no puede ser nulo");
        return areaRepository.findAreaByDepartamentsContaining(department);
    }

    public List<Area> findAllByDepartamentsContaining(Department department){
        Assert.notNull(department, "El departamento no puede ser nulo");
        return areaRepository.findAllByDepartamentsContains(department);
    }

}