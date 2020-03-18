package com.mangobits.startupkit.social.survey;

import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostDAO;
import com.mangobits.startupkit.user.User;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SurveyServiceImpl implements SurveyService {

    @New
    @Inject
    private PostDAO postDAO;


    @Override
    public Post saveVote (SurveyOption surveyOption, User user) throws Exception {

        if(surveyOption.getIdPost() == null){
            throw new BusinessException("missing_idPost");
        }

        if(surveyOption.getId() == null){
            throw new BusinessException("missing_idSurvey");
        }
        // adiciona o comentário no postInfo
        Post post = postDAO.retrieve(new Post(surveyOption.getIdPost()));
        if (post == null){
            throw new BusinessException("post_not_found");
        }
        if (post.getSurvey() == null){
            throw new BusinessException("no_survey_found");
        }

        String idUser = post.getSurvey().getListUsers().stream()
                .filter(p -> p.equals(user.getId()))
                .findFirst()
                .orElse(null);

        if (idUser != null){
            throw new BusinessException("user_already_answered_survey");
        }

        if (post.getSurvey().getListSurveyOptions() == null){
            throw new BusinessException("no_surveyOptions_found");
        }

        SurveyOption surveyOptionBase = post.getSurvey().getListSurveyOptions().stream()
                .filter(p -> p.getId().equals(surveyOption.getId()))
                .findFirst()
                .orElse(null);

        if (surveyOptionBase == null){
            throw new BusinessException("surveyOption_not_found");
        }
        if (surveyOptionBase.getNumberOfVotes() == null){
            surveyOptionBase.setNumberOfVotes(0d);
        }
        if (post.getSurvey().getTotalVotes() == null){
            post.getSurvey().setTotalVotes(0);
        }
        surveyOptionBase.setNumberOfVotes(surveyOptionBase.getNumberOfVotes() + 1);

        post.getSurvey().setTotalVotes(post.getSurvey().getTotalVotes() + 1);

        post.getSurvey().getListUsers().add(user.getId());


        for (SurveyOption item: post.getSurvey().getListSurveyOptions()){
            if (item.getNumberOfVotes() == null){
                item.setNumberOfVotes(0d);
            }
            item.setPercentageOfVotes(item.getNumberOfVotes()/post.getSurvey().getTotalVotes() * 100);
        }

        postDAO.update(post);
        return post;

    }


}
