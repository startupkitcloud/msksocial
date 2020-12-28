package org.startupkit.social.survey;

import org.startupkit.core.exception.BusinessException;
import org.startupkit.social.post.Post;
import org.startupkit.user.User;
import org.startupkit.user.util.SecuredUser;
import org.startupkit.user.util.UserBaseRestService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("/survey")
public class SurveyRestService  extends UserBaseRestService {

    @EJB
    private SurveyService surveyService;

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/saveVote")
    public Post saveVote(SurveyOption surveyOption)  throws Exception{

        User user = getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_not_found");
        }

        Post post = surveyService.saveVote(surveyOption, user);
        return post;
    }
}
