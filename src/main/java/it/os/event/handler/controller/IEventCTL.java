package it.os.event.handler.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.os.event.handler.entity.EventETY;
import it.os.event.handler.entity.EventRequest;
import it.os.event.handler.entity.StepETY;

/**
 * Interface of event controller.
 */
@RequestMapping("/v1.0.0")
@CrossOrigin(origins = {"${allowed-cross-orgin}", "${extra-allowed-cross-origin}"})
public interface IEventCTL {

        @Operation(summary = "Generate a new event and persist it", description = "Generate a new event and persist it", tags = { "Event" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Event saved"),
                        @ApiResponse(responseCode = "400", description = "Invalid event data"),
                        @ApiResponse(responseCode = "500", description = "Error while saving event")
        })
        @PostMapping("/event")
        ResponseEntity<String> createEvent(@RequestBody(required = true) EventRequest turbineData, HttpServletRequest request);

        @Operation(summary = "Updates an existing event", description = "Updates an existing event", tags = { "Event" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Event updated"),
                        @ApiResponse(responseCode = "404", description = "Event not found"),
                        @ApiResponse(responseCode = "500", description = "Error while updating event")
        })
        @PutMapping("/event")
        ResponseEntity<String> updateEvent(@RequestBody(required = true) EventRequest turbineData, HttpServletRequest request);


        @Operation(summary = "Deletes an event identified by its Id", description = "Deletes an event identified by its Id", tags = { "Event" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Event deleted"),
                        @ApiResponse(responseCode = "500", description = "Error while deleting event")
        })
        @DeleteMapping("/event")
        ResponseEntity<Void> deleteEvent(@RequestParam(value = "eventId") Integer eventId, HttpServletRequest request);

        @Operation(summary = "Returns all events ordered by completion percentage", description = "Returns all events ordered by completion percentage", tags = { "Event" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Events retrieved"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving events")
        })
        @GetMapping("/events")
        ResponseEntity<List<EventETY>> getAllEvents(@RequestParam(required = false) boolean includeCompleted, HttpServletRequest request);

        @Operation(summary = "Returns all steps for a given event", description = "Returns all steps for a given event", tags = { "Step" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Steps retrieved"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving steps")
        })
        @GetMapping("/steps/{eventId}")
        ResponseEntity<List<StepETY>> getStepsByEventId(@PathVariable(value = "eventId") Integer eventId, HttpServletRequest request);

        @Operation(summary = "Set a step in to a complete state", description = "Set a step in to a complete state", tags = {"Step" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Step updated"),
                        @ApiResponse(responseCode = "404", description = "Step not found"),
                        @ApiResponse(responseCode = "500", description = "Error while updating step")
        })
        @PutMapping("/step/complete")
        ResponseEntity<String> setStepCompletion(@RequestParam(value = "stepId") Integer stepId,
                        @RequestParam(value = "isCompleted") Boolean isComplete, HttpServletRequest request);

        @Operation(summary = "Returns all steps", description = "Returns all existing sptes", tags = { "Step" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Steps retrieved"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving steps")
        })
        @GetMapping("/steps")
        ResponseEntity<List<StepETY>> getSteps(HttpServletRequest request);

        @Operation(summary = "Extract data about turbines", description = "Returns a csv files with every information on turbines", tags = { "Event" })
        @ApiResponse(content = @Content(mediaType ="text/csv", schema = @Schema(implementation = byte.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Extraction complete"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/events/csv")
        ResponseEntity<byte[]> getAllEventsAsCsv(HttpServletRequest request);

        @Operation(summary = "Returns all events completed", description = "Returns all events completed", tags = { "Event" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Events retrieved"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving events")
        })
        @GetMapping("/events/completed")
        ResponseEntity<List<EventETY>> getAllCompletedEvents(HttpServletRequest request);
}
