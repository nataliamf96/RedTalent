package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Area;
import src.redtalent.repositories.AreaRepository;
import java.util.Collection;
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

    public Collection<Area> findAll() {
        Collection<Area> result = areaRepository.findAll();
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
}