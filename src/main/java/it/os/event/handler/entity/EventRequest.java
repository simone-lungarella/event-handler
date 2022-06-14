package it.os.event.handler.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import it.os.event.handler.enums.TurbineStateEnum;
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
    
    private String description;
    
    private String operation;
   
    private TurbineStateEnum turbineState;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDateEEMM;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDateOOCC;
}
