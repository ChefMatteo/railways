package it.uniprisma.exercise4dot2.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.models.wagon.*;
import it.uniprisma.exercise4dot2.models.wagon.enums.FuelType;
import it.uniprisma.exercise4dot2.models.wagon.enums.WagonClass;
import it.uniprisma.exercise4dot2.services.WagonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wagons")
public class WagonController {

    private final WagonService wagonService;

    public WagonController(WagonService wagonService) {
        this.wagonService = wagonService;
    }

    @Operation(summary = "Add a new wagon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(allOf = Wagon.class, oneOf = {RestaurantWagon.class, PassengerWagon.class, BedWagon.class, MotorWagon.class}))}),
            @ApiResponse(responseCode = "409", description = "Already exists a train with id in body", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Wagon createTrain(@RequestBody AllOfWagon wagon) {
        return wagonService.createSingleWagon(wagon);
    }

    @Operation(summary = "Get wagons list with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<Wagon> findWagonPage(@RequestParam(required = false) Boolean bathroom,
                                              @RequestParam(required = false) WagonClass wagonClass,
                                              @RequestParam(required = false) Integer minimumTables,
                                              @RequestParam(required = false) String kitchenType,
                                              @RequestParam(required = false) FuelType fuelType,
                                              @RequestParam(required = false) Integer minimumBeds,
                                              @RequestParam(required = false) String bedType,
                                              @RequestParam(required = false) Integer minimumSeats,
                                              @RequestParam Integer offset,
                                              @RequestParam Integer limit) {
        return wagonService.findPage(bathroom, wagonClass, minimumTables, kitchenType, fuelType,
                                     minimumBeds, bedType, minimumSeats, offset, limit);
    }

    @Operation(summary = "Get an existing wagon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Wagon"))}),
            @ApiResponse(responseCode = "404", description = "No wagon to get", content = @Content)})
    @GetMapping("/{wagonId}")
    @ResponseStatus(HttpStatus.OK)
    public Wagon getSingleWagon(@PathVariable("wagonId") String wagonId ){
        return wagonService.getSingle(wagonId);
    }

    @Operation(summary = "Update an existing wagon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wagon successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Wagon"))}),
            @ApiResponse(responseCode = "400", description = "Parameters not available for type of wagon in body", content = @Content),
            @ApiResponse(responseCode = "404", description = "No wagon to update", content = @Content)})
    @PutMapping("/{wagonId}")
    @ResponseStatus(HttpStatus.OK)
    public Wagon updateSingleWagon(@PathVariable("wagonId") String wagonId,
                                   @RequestBody AllOfWagon wagon){
        return wagonService.updateSingleWagon(wagon, wagonId);
    }

    @Operation(summary = "Delete an existing wagon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "No wagon to delete")})
    @DeleteMapping("/{wagonId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteWagon(@PathVariable("wagonId") String wagonId ){
        wagonService.deleteSingle(wagonId);
    }

}