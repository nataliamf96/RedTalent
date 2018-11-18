package src.redtalent.services;

import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import src.redtalent.domain.Alert;
import src.redtalent.repositories.AlertRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AlertService {

    @Autowired
    private AlertRepository alertRepository;

    public AlertService(){
        super();
    }

    public Alert create(){
        Alert alert = new Alert();
        return alert;
    }

    public Alert findOne(String alertId) {
        Assert.notNull(alertId, "Alert Service : id null");
        Optional<Alert> result = alertRepository.findById(alertId);
        return result.get();
    }

    public List<Alert> findAll() {
        List<Alert> result = alertRepository.findAll();
        Assert.notNull(result, "Alert Service : list null");
        return result;
    }

    public Alert save(Alert alert) {
        Assert.notNull(alert, "Alert Service : Objeto null");
        Alert result = alertRepository.save(alert);
        return result;
    }

    public void remove(Alert alert) {
        Assert.notNull(alert, "Alert Service : Objeto null");
        alertRepository.delete(alert);
    }

}
