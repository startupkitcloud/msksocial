package com.mangobits.startupkit.social.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostService;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.util.SecuredUser;
import com.mangobits.startupkit.user.util.UserBaseRestService;
import com.mangobits.startupkit.ws.JsonContainer;

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
    public String saveVote(SurveyOption surveyOption)  throws Exception{

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            User user = getUserTokenSession();
            if (user == null){
                throw new BusinessException("user_not_found");
            }

            Post post = surveyService.saveVote(surveyOption, user);
            cont.setData(post);

        } catch (Exception e) {
            handleException(cont, e, "saving sourvey option");
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }
}
