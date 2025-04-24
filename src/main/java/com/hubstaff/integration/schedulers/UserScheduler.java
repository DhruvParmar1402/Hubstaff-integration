package com.hubstaff.integration.schedulers;


import com.hubstaff.integration.service.user.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserScheduler {

    private Logger logger= LoggerFactory.getLogger(UserScheduler.class);
    private final UserServiceImpl userServiceImpl;

    public UserScheduler(UserServiceImpl userServiceImpl)
    {
        this.userServiceImpl = userServiceImpl;
    }

//    @Scheduled(cron = "0 0 0 * * *")
@Scheduled(fixedRate = 5000)
    public void fetchAndSaveUsers()

    {
        logger.info("User scheduler executed.");
        userServiceImpl.fetchAndSaveUsers();
    }
}
