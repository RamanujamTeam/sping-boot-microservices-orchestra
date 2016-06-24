package in.ramanujam;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author Roma
 */
@Service
public class Scheduler {

    @Scheduled(fixedDelay = 1000)
    public void runWithDelay() {
        System.out.println("Hello!!!");
    }
}
