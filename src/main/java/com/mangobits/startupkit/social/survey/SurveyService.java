package com.mangobits.startupkit.social.survey;

import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.user.User;

import javax.ejb.Local;

@Local
public interface SurveyService {

    Post saveVote(SurveyOption surveyOption, User user) throws Exception;

}
