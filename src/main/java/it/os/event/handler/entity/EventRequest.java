package it.os.event.handler.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    private String turbineName;

    private String turbineNumber;

    private Integer odlNumber;
    
    private String description;

    private String power;
    
    private List<String> operation;
   
    private String turbineState;

    private Boolean toNotDismantle;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDateEEMM;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDateOOCC;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate permittingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate priorNotification;
}
