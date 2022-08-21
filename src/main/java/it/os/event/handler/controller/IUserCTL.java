package it.os.event.handler.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import it.os.event.handler.entity.RegistrationRequest;

/**
 * Interface of user controller.
 */
@RequestMapping("/v1.0.0/registration")
@CrossOrigin(origins = "${allowed-cross-orgin}")
public interface IUserCTL {

        @Operation(summary = "Registrate a new user", description = "Generate a new user and persist its information", tags = { "User" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User registered"),
                        @ApiResponse(responseCode = "409", description = "User already registered"),
                        @ApiResponse(responseCode = "500", description = "Error while registrating new user")
        })
        @PostMapping("/user")
        ResponseEntity<String> createUser(@RequestBody(required = true) RegistrationRequest registrationRequest, HttpServletRequest request);

        @Operation(summary = "Retrieve user", description = "Retrieve information on user", tags = { "User" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDetails.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User retrieved"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving user data")
        })
        @GetMapping("/user")
        UserDetails getUser(@RequestParam(value = "username") String username, HttpServletRequest request);


        @Operation(summary = "Retrieve all user", description = "Retrieve information of all users", tags = { "User" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDetails.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved"),
                        @ApiResponse(responseCode = "401", description = "Operation unauthorized"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving user data")
        })
        @GetMapping("/users")
        List<UserDetails> getAllUsers(HttpServletRequest request);

        @Operation(summary = "Delete a user", description = "Delete a user having a specific username", tags = { "User" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDetails.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users deleted"),
                        @ApiResponse(responseCode = "401", description = "Operation unauthorized"),
                        @ApiResponse(responseCode = "500", description = "Error while deleting user data")
        })
        @DeleteMapping("/user")
        ResponseEntity<String> deleteUser(@RequestParam(value = "username") String username, HttpServletRequest request);

        @Operation(summary = "Retrieve all user permissions", description = "Retrieve permissions of a user", tags = { "User" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User authorization retrieved"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving user data")
        })
        @GetMapping("/user-auth")
        ResponseEntity<String> getUserAuth(@RequestParam(value = "username") String username, HttpServletRequest request);

        @Operation(summary = "Updates all user permissions", description = "Updates permissions of a user", tags = { "User" })
        @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Void.class)))
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User authorization updated"),
                        @ApiResponse(responseCode = "500", description = "Error while retrieving user data")
        })
        @PutMapping("/user-auth")
        ResponseEntity<Void> updateUserAuth(@RequestParam(value = "username") String username, @RequestParam(value = "auth") String auth, HttpServletRequest request);
}
