package it.os.event.handler.entity;

import java.time.LocalDate;

import it.os.event.handler.enums.OperationTypeEnum;
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
    
    OperationTypeEnum operation;
   
    TurbineStateEnum turbineState;
    
    LocalDate startingDateEEMM;
    
    LocalDate startingDateOOCC;
}
