package com.mangobits.startupkit.social.spider;


import javax.ejb.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class SpiderBot {


    @EJB
    private SpiderService spiderService;


    @Timeout
    @AccessTimeout(value = 20, unit = TimeUnit.MINUTES)
    @Schedules({
            @Schedule(dayOfMonth = "*", minute = "*", hour = "*/8", second="0", persistent=false)
    })
    public void process() {

        try {
            spiderService.goSpider();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}