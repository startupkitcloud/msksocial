package com.mangobits.startupkit.social.post;

import javax.ejb.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class PendingPostBot {

    @EJB
    private PostService postService;


    @Timeout
    @AccessTimeout(value = 20, unit = TimeUnit.MINUTES)
    @Schedules({
            @Schedule(dayOfMonth = "*", minute = "*", hour = "*/24", second="0", persistent=false)
    })
    public void process() {

        try {
            postService.blockExpiredPendingPosts();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
