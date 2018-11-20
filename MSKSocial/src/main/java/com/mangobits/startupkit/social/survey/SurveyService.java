package com.mangobits.startupkit.social.survey;

import com.mangobits.startupkit.social.post.Post;

import javax.ejb.Local;

@Local
public interface SurveyService {

    Post saveVote(SurveyOption surveyOption) throws Exception;
}
