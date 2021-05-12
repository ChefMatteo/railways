package it.uniprisma.exercise4dot2.services;

import com.google.gson.Gson;
import it.uniprisma.exercise4dot2.components.ConfigurationComponent;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.models.Route;
import it.uniprisma.exercise4dot2.models.train.Train;
import it.uniprisma.exercise4dot2.models.train.TrainType;
import it.uniprisma.exercise4dot2.models.wagon.*;
import it.uniprisma.exercise4dot2.models.wagon.enums.FuelType;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonClass;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonType;
import it.uniprisma.exercise4dot2.utils.BadRequestException;
import it.uniprisma.exercise4dot2.utils.ConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class TrainService extends BaseService<Train> {
    private RouteService routeService;
    private WagonService wagonService;

    public TrainService(ConfigurationComponent configurationComponent,
                        Gson gson) {
        super.config = configurationComponent;
        super.gson = gson;
        super.getterMethodOfPrimaryKey = "getId";
    }

    @Autowired
    public void setRouteService(RouteService routeService){
        this.routeService = routeService;
    }
    @Autowired
    public void setWagonService(WagonService wagonService){
        this.wagonService = wagonService;
    }

    @PostConstruct
    private void initTrain() {
        init(Train.class, "/train.json");
    }

    public PagedResponse<Train> findTrains(String companyName, TrainType trainType, Integer offset, Integer limit) {
        List<Train> filtredList = list.stream()
                .filter(t -> {
                    if (companyName != null) return t.getCompanyName().equalsIgnoreCase(companyName);
                    else return true;
                })
                .filter(t -> {
                    if (trainType != null) return t.getTrainType() == trainType;
                    else return true;
                })
                .toList();
        return findPage(filtredList, offset, limit);
    }


    public void deleteTrain(String trainId) {
        deleteTrainRouteAssociation(Collections.singletonList(trainId), getSingle(trainId).getRoutesId());
        deleteSingle(trainId);
    }

    public PagedResponse<Wagon> findWagonsOfTrainPage(String trainId, WagonType wagonType, String kitchenType, FuelType fuelType,
                                                      Boolean bathroom, WagonClass wagonClass, String bedType, Integer minimumTables,
                                                      Integer minimumBeds, Integer minimumSeats, Integer offset, Integer limit) {
        List<Wagon> filtredList = wagonService.list.stream()
                .filter(w-> {
                    if (trainId != null) return w.getTrainId().equalsIgnoreCase(trainId);
                    else return true;
                })
                .filter(w-> {
                    if (wagonType != null) return w.getWagonType()==wagonType;
                    else return true;
                })
                .filter(w -> {
                    if (bathroom != null) return w.getBathroom()==bathroom;
                    else return true;
                })
                .filter(w -> {
                    if (wagonClass != null) return w.getWagonClass()==wagonClass;
                    else return true;
                })
                .filter(w -> {
                    if (minimumTables != null && w.getClass()== RestaurantWagon.class)
                        return ((RestaurantWagon) w).getNTables()>=minimumTables;
                    else return true;
                })
                .filter(w -> {
                    if (kitchenType != null && w.getClass()== RestaurantWagon.class)
                        return ((RestaurantWagon) w).getKitchenType().equalsIgnoreCase(kitchenType);
                    else return true;
                })
                .filter(w -> {
                    if (fuelType != null && w.getClass()== MotorWagon.class)
                        return ((MotorWagon) w).getFuelType()==fuelType;
                    else return true;
                })
                .filter(w -> {
                    if (minimumBeds != null && w.getClass()== BedWagon.class)
                        return ((BedWagon) w).getNBeds()>=minimumBeds;
                    else return true;
                })
                .filter(w -> {
                    if (bedType != null && w.getClass()== BedWagon.class)
                        return ((BedWagon) w).getBedType().equalsIgnoreCase(bedType);
                    else return true;
                })
                .filter(w -> {
                    if (minimumSeats != null && w.getClass()== PassengerWagon.class)
                        return ((PassengerWagon) w).getNSeats()>=minimumSeats;
                    else return true;
                })
                .toList();
        return wagonService.findPage(filtredList, offset, limit);
    }

    public PagedResponse<Route> findRoutesOfTrainPage(String trainId, String stationName, String binaryType, Double minimumLength,
                                                      Double maxLength, Integer offset, Integer limit) {
        List<Route> filtredList = routeService.list.stream()
                .filter(r-> r.getTrainsId().contains(trainId))
                .filter(r -> {
                    if (stationName != null) return r.getStartStationName().equalsIgnoreCase(stationName) ||
                            r.getDestinationStationName().equalsIgnoreCase(stationName);
                    else return true;
                })
                .filter(r -> {
                    if (binaryType != null) return r.getBinaryType().equalsIgnoreCase(binaryType);
                    else return true;
                })
                .filter(r -> {
                    if (minimumLength != null) return r.getLength() >= minimumLength;
                    else return true;
                })
                .filter(r -> {
                    if (maxLength != null) return r.getLength() <= maxLength;
                    else return true;
                })
                .toList();
        return routeService.findPage(filtredList, offset, limit);
    }

    public void createTrainRouteAssociation(String trainId, String routeId) {
        if(!getSingle(trainId).getRoutesId().contains(routeId)) {
            getSingle(trainId).getRoutesId().add(routeId);
            routeService.getSingle(routeId).getTrainsId().add(trainId);

            updateSingle(getSingle(trainId), trainId);
            routeService.updateSingle(routeService.getSingle(routeId), routeId);
        }
        else throw new ConflictException("routeId", routeId);

    }

    public void deleteTrainRouteAssociation(List<String> trainIds, List<String> routeIds) {
        if(trainIds.stream().anyMatch(tId->getSingle(tId).getRoutesId().retainAll(routeIds)) &&
            routeIds.stream().anyMatch(rId->routeService.getSingle(rId).getTrainsId().retainAll(trainIds))){
            updateJson();
            routeService.updateJson();
        }
        else throw new BadRequestException("TrainId or routeId not valid");
    }

    public void createTrainWagonAssociation(String trainId, String wagonId) {
        if(!wagonService.getSingle(wagonId).getTrainId().contains(trainId)) {
            if(list.stream().noneMatch(t->t.getId().equalsIgnoreCase(trainId))){
                throw new BadRequestException("TrainId not valid");
            }
            wagonService.getSingle(wagonId).setTrainId(trainId);
            wagonService.updateSingle(wagonService.getSingle(wagonId), wagonId);
        }
        else throw new ConflictException("trainId", trainId);

    }

    public void deleteTrainWagonAssociation(List<String> trainIds, List<String> wagonIds) {
        if(trainIds.stream().anyMatch(tId->getSingle(tId).getRoutesId().retainAll(wagonIds)) &&
                wagonIds.stream().anyMatch(wId->routeService.getSingle(wId).getTrainsId().retainAll(trainIds))){
            updateJson();
            wagonService.updateJson();
        }
        else throw new BadRequestException("TrainId or wagonId not valid");
    }
}
