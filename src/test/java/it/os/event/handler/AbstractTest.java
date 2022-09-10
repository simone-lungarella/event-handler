package it.os.event.handler;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import it.os.event.handler.entity.EventRequest;
import it.os.event.handler.enums.OperationTypeEnum;
import it.os.event.handler.enums.TurbinePower;
import it.os.event.handler.enums.TurbineStateEnum;

public abstract class AbstractTest {

    private EventRequest getRequestObj (final String turbineName, final String turbineNumber, final String description, final TurbinePower power, 
        final List<String> operations, final TurbineStateEnum turbineState, final LocalDate permittingDate) {

        EventRequest request = new EventRequest();
        request.setTurbineName(turbineName);
        request.setTurbineNumber(turbineNumber);
        request.setOdlNumber(null);
        request.setDescription(description);
        request.setPower(power.getName());
        request.setOperation(operations);
        request.setTurbineState(turbineState.getName());
        request.setStartingDateEEMM(LocalDate.now());
        request.setStartingDateOOCC(LocalDate.now());
        request.setPermittingDate(permittingDate);
        return request;
    }

    protected EventRequest getRandomEventRequest() {
        final List<String> operations = Arrays.asList(OperationTypeEnum.SOST_GENERATORE.getDescription());
        return getRequestObj("Turbine name", "XXXX", "eventDescription", TurbinePower.MEGAWATT, operations, TurbineStateEnum.LIMITED, null);
    }

    protected EventRequest getEventForScheduler(final String turbineName, final List<String> operations, final LocalDate permittingDate) {
        return getRequestObj(turbineName, "XXXX", "eventDescription", TurbinePower.MEGAWATT, operations, TurbineStateEnum.LIMITED, permittingDate);

    }
}
