package com.hubstaff.integration.schedulers;

import com.hubstaff.integration.service.token.TokenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



@Component
public class TokenScheduler {

    private Logger logger=LoggerFactory.getLogger(TokenScheduler.class);
    private final TokenServiceImpl tokenServiceImpl;

    public TokenScheduler(TokenServiceImpl tokenServiceImpl)
    {
        this.tokenServiceImpl = tokenServiceImpl;
    }

//    @Scheduled(fixedRate =  60 * 60 * 1000)
@Scheduled(fixedRate = 5000)
    public void getNewAccessToken()
    {
        logger.info("Token scheduler executed.");
        tokenServiceImpl.refreshToken();
    }
}
