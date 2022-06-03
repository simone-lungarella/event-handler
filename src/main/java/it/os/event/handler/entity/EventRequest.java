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

    String turbineName;
    
    String description;
    
    String operation;
   
    TurbineStateEnum turbineState;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startingDateEEMM;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate startingDateOOCC;
}
