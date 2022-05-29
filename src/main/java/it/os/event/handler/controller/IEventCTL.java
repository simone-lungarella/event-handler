package it.os.event.handler.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.StepETY;
import it.os.event.handler.enums.OperationTypeEnum;
import it.os.event.handler.enums.StepTypeEnum;

/**
 * Interface of event controller.
 * 
 * @author Simone Lungarella
 */
@RequestMapping("/v1.0.0")
public interface IEventCTL {

    @Operation(summary = "Generate a new event and persist it", description = "Generate a new event and persist it", tags = { "Event" })
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event saved"),
            @ApiResponse(responseCode = "500", description = "Error while saving event")
    })
    @PostMapping("/event")
    ResponseEntity<String> createEvent(
            @RequestParam(value = "name", required = true) String name, 
            @RequestParam(value = "turbineName", required = true) String turbineName,
            @RequestParam(value = "operation", required = true) OperationTypeEnum operation, 
            @RequestParam(value = "description", required = true) String description,
            @RequestParam(value = "startEEMM", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startingDateEEMM,
            HttpServletRequest request);

    @Operation(summary = "Deletes an event identified by its Id", description = "Deletes an event identified by its Id", tags = { "Event" })
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event deleted"),
            @ApiResponse(responseCode = "500", description = "Error while deleting event")
    })
    @DeleteMapping("/event")
    ResponseEntity<Void> deleteEvent(@RequestParam(value = "eventId") String eventId, HttpServletRequest request);

    @Operation(summary = "Returns all events ordered by completion percentage", description = "Returns all events ordered by completion percentage", tags = { "Event" })
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving events")
    })
    @GetMapping("/events")
    ResponseEntity<List<EventETY>> getEntities(HttpServletRequest request);

    @Operation(summary = "Returns all events that reached a specific step", description = "Returns all events that reached a specific step", tags = { "Event" })
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Events retrieved"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving events")
    })
    @GetMapping("/events/step-reached")
    ResponseEntity<List<EventETY>> getEntities(@RequestParam(value = "reachedStep") StepTypeEnum reachedStep, HttpServletRequest request);

    @Operation(summary = "Returns all steps for a given event", description = "Returns all steps for a given event", tags = { "Step" })
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Steps retrieved"),
            @ApiResponse(responseCode = "500", description = "Error while retrieving steps")
    })
    @GetMapping("/steps")
    ResponseEntity<List<StepETY>> getSteps(@RequestParam(value = "eventId") String eventId, HttpServletRequest request);

    @Operation(summary = "Set a step in to a complete state", description = "Set a step in to a complete state", tags = { "Step" })
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Step updated"),
            @ApiResponse(responseCode = "404", description = "Step not found"),
            @ApiResponse(responseCode = "500", description = "Error while updating step")
    })
    @PutMapping("/step/complete")
    ResponseEntity<String> setStepComplete(@RequestParam(value = "stepId") String stepId, HttpServletRequest request);
    
}
