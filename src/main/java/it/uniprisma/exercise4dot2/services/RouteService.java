package it.uniprisma.exercise4dot2.services;

import com.google.gson.Gson;
import it.uniprisma.exercise4dot2.components.ConfigurationComponent;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.models.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class RouteService extends BaseService<Route>{
    private TrainService trainService;

    public RouteService(ConfigurationComponent configurationComponent,
                        Gson gson) {
        super.config = configurationComponent;
        super.gson = gson;
        super.getterMethodOfPrimaryKey = "getId";
    }

    @Autowired
    public void setTrainService(TrainService trainService){
        this.trainService = trainService;
    }

    @PostConstruct
    private void initRoute() {
        init(Route.class, "/route.json");
    }


    public PagedResponse<Route> findRoutesPage(String stationName, String destinationStationName, String startStationName,
                                               double minimumLength, double maxLength, Integer offset, Integer limit) {
        List<Route> filtredList = list.stream()
                .filter(r -> {
                    if (stationName != null) return r.getStartStationName().equalsIgnoreCase(stationName) ||
                            r.getDestinationStationName().equalsIgnoreCase(stationName);
                    else return true;
                })
                .filter(r -> {
                    if (destinationStationName != null) return r.getDestinationStationName().equalsIgnoreCase(destinationStationName);
                    else return true;
                })
                .filter(r -> {
                    if (startStationName != null) return r.getStartStationName().equalsIgnoreCase(startStationName);
                    else return true;
                })
                .filter(r -> {
                    if (minimumLength != 0) return r.getLength()>=minimumLength;
                    else return true;
                })
                .filter(r -> {
                    if (maxLength != 0) return r.getLength()<=maxLength;
                    else return true;
                })
                .toList();
        return findPage(filtredList, offset, limit);
    }

    public void deleteRoute(String routeId) {
        trainService.list
                .forEach(t->t.getRoutesId().remove(routeId));
        deleteSingle(routeId);
    }
}
