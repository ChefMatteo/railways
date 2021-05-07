package it.uniprisma.exercise4dot2.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.models.Route;
import it.uniprisma.exercise4dot2.models.train.Train;
import it.uniprisma.exercise4dot2.models.train.TrainType;
import it.uniprisma.exercise4dot2.models.wagon.enums.FuelType;
import it.uniprisma.exercise4dot2.models.wagon.Wagon;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonClass;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonType;
import it.uniprisma.exercise4dot2.services.RouteService;
import it.uniprisma.exercise4dot2.services.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trains")
public class TrainController {

    private static TrainService trainService;

    public TrainController(TrainService trainService, RouteService routeService) {
        this.trainService = trainService;
    }

    @Operation(summary = "Add a new train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Train"))}),
            @ApiResponse(responseCode = "409", description = "Already exists a train with id in body")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Train createTrain(@RequestBody Train train) {
        return trainService.createSingleTrain(train);
    }

    @Operation(summary = "Get trains list with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<Train> findTrainPage(@RequestParam(required = false) String companyName,
                                              @RequestParam(required = false) TrainType trainType,
                                              @RequestParam Integer offset,
                                              @RequestParam Integer limit) {
        return trainService.findTrains(companyName, trainType, offset, limit);
    }

    @Operation(summary = "Get an existing train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Train"))}),
            @ApiResponse(responseCode = "404", description = "No train to get")})
    @GetMapping("/{trainId}")
    @ResponseStatus(HttpStatus.OK)
    public Train getSingleTrain(@PathVariable("trainId") String trainId ){
        return trainService.getSingleTrain(trainId);
    }

    @Operation(summary = "Update an existing train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Train successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Train"))}),
            @ApiResponse(responseCode = "404", description = "No train to update")})
    @PutMapping("/{trainId}")
    @ResponseStatus(HttpStatus.OK)
    public Train updateSingleTrain(@PathVariable("trainId") String trainId,
                                   @RequestBody Train train){
        return trainService.updateSingleTrain(trainId, train);
    }

    @Operation(summary = "Delete an existing train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "No train to delete")})
    @DeleteMapping("/{trainId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrain(@PathVariable("trainId") String trainId ){
        trainService.deleteTrain(trainId);
    }

    @Operation(summary = "Get wagons list of a specific train with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponse.class))}),
            @ApiResponse(responseCode = "404", description = "No train to get")})
    @GetMapping("/{trainId}/wagons")
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<Wagon> findWagonsOfTrainPage(@PathVariable("trainId") String trainId,
                                              @RequestParam(required = false) WagonType wagonType,
                                              @RequestParam(required = false) String kitchenType,
                                              @RequestParam(required = false) FuelType fuelType,
                                              @RequestParam(required = false) Boolean bathroom,
                                              @RequestParam(required = false) WagonClass wagonClass,
                                              @RequestParam(required = false) String bedType,
                                              @RequestParam(required = false) Integer minimumTables,
                                              @RequestParam(required = false) Integer minimumBeds,
                                              @RequestParam(required = false) Integer minimumSeats,
                                              @RequestParam Integer offset,
                                              @RequestParam Integer limit) {
        return trainService.findWagonsOfTrainPage(trainId, wagonType, kitchenType, fuelType,
                                                  bathroom, wagonClass, bedType, minimumBeds, minimumTables, minimumSeats, offset, limit);
    }

    @Operation(summary = "Get routes list of a specific train with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponse.class))}),
            @ApiResponse(responseCode = "404", description = "No train to get")})
    @GetMapping("/{trainId}/routes")
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<Route> findWagonsOfTrainPage(@PathVariable("trainId") String trainId,
                                                      @RequestParam(required = false) String stationName,
                                                      @RequestParam(required = false) String binaryType,
                                                      @RequestParam(required = false) Double minimumLength,
                                                      @RequestParam(required = false) Double maxLength,
                                                      @RequestParam Integer offset,
                                                      @RequestParam Integer limit) {
        return trainService.findRoutesOfTrainPage(trainId, stationName, binaryType, minimumLength, maxLength, offset, limit);
    }

    @Operation(summary = "Add a new association between route and train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "TrainRouteAssociation"))}),
            @ApiResponse(responseCode = "409", description = "Already exists an association between ids in path")})
    @PostMapping("/{trainId}/routes/{routeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrainRouteAssociation(@PathVariable("trainId") String trainId, @PathVariable("routeId") String routeId) {
         trainService.createTrainRouteAssociation(trainId, routeId);
    }

    @Operation(summary = "Delete an existing association between train and route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "No association to delete")})
    @DeleteMapping("/{trainId}/routes/{routeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainRouteAssociation(@PathVariable("trainId") String trainId, @PathVariable("routeId") String routeId) {
         trainService.deleteTrainRouteAssociation(trainId, routeId);
    }

    @Operation(summary = "Add or rewrite a new association between wagon and train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "TrainRouteAssociation"))}),
            @ApiResponse(responseCode = "409", description = "Already exists an association between ids in path")})
    @PostMapping("/{trainId}/wagon/{wagonId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrainWagonAssociation(@PathVariable("trainId") String trainId, @PathVariable("wagonId") String wagonId) {
         trainService.createTrainWagonAssociation(trainId, wagonId);
    }

    @Operation(summary = "Delete an existing association between wagon and train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "No association to delete")})
    @DeleteMapping("/{trainId}/wagon/{wagonId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrainWagonAssociation(@PathVariable("trainId") String trainId, @PathVariable("wagonId") String wagonId) {
         trainService.deleteTrainWagonAssociation(trainId, wagonId);
    }


}