package it.uniprisma.exercise4dot2.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.uniprisma.exercise4dot2.models.PagedResponse;
import it.uniprisma.exercise4dot2.models.Route;
import it.uniprisma.exercise4dot2.services.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @Operation(summary = "Add a new route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Route"))}),
            @ApiResponse(responseCode = "409", description = "Already exists a route with id in body", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Route createRoute(@RequestBody Route route) {
        return routeService.createNew(route);
    }

    @Operation(summary = "Get routes list with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PagedResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<Route> findTrainPage(@RequestParam(required = false) String destinationStationName,
                                              @RequestParam(required = false) String startStationName,
                                              @RequestParam(required = false) String binaryType,
                                              @RequestParam(required = false) double minimumLength,
                                              @RequestParam(required = false) double maxLength,
                                              @RequestParam Integer offset,
                                              @RequestParam Integer limit) {
        return routeService.findRoutesPage(destinationStationName, startStationName, binaryType, minimumLength, maxLength, offset, limit);
    }

    @Operation(summary = "Get an existing route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Route"))}),
            @ApiResponse(responseCode = "404", description = "No route to get", content = @Content)})
    @GetMapping("/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public Route getSingleRoute(@PathVariable("routeId") String routeId){
        return routeService.getSingle(routeId);
    }

    @Operation(summary = "Update an existing route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(type = "object", ref = "Route"))}),
            @ApiResponse(responseCode = "404", description = "No route to update", content = @Content)})
    @PutMapping("/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public Route updateSingleTrain(@PathVariable("routeId") String routeId,
                                   @RequestBody Route route){
        return routeService.updateSingle(route, routeId);
    }

    @Operation(summary = "Delete an existing route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "No route to delete")})
    @DeleteMapping("/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRoute(@PathVariable("routeId") String routeId ){
        routeService.deleteRoute(routeId);
    }

}