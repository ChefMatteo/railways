package it.uniprisma.exercise4dot2.services;

import com.google.gson.Gson;
import it.uniprisma.exercise4dot2.components.ConfigurationComponent;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.models.Route;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
public class RouteService extends BaseService<Route>{
    public static Resource routeResource;
    private TrainService trainService;

    public RouteService(ConfigurationComponent configurationComponent,
                        Gson gson) {
        super.config = configurationComponent;
        super.gson = gson;
    }

    @Autowired
    public void setTrainService(TrainService trainService){
        this.trainService = trainService;
    }

    @SneakyThrows
    @PostConstruct
    private void initRoute() {
        routeResource = init(Route.class, "/route.json");
    }


    public Route createRoute(Route route) {
        return createNew(route, routeResource);
    }

    public PagedResponse<Route> findRoutesPage(String stationName, String destinationStationName, String startStationName, double minimumLength, double maxLength, Integer offset, Integer limit) {
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

    public Route getSingleRoute(String routeId) {
        return getSingle(routeId);
    }

    public Route updateSingleRoute(String routeId, Route route) {
        return updateSingle(route, routeId, routeResource);
    }

    @SneakyThrows
    public void deleteRoute(String routeId) {
        deleteSingle(routeId, routeResource);
    }
}
