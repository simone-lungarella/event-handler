package it.os.event.handler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class SchedulerCFG {
    
    @Value("${event.kw-threshold-days}")
    private Integer kiloWThreshold;

    @Value("${event.mw-threshold-days}")
    private Integer megaWThreshold;

<<<<<<< HEAD
    @Value("${event.days-threshold-limit}")
=======
    @Value("${event.threshold-days-limit}")
>>>>>>> develop
    private Integer thresholdLimit;

}
