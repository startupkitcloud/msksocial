package com.mangobits.startupkit.social.survey;

import javax.ejb.Local;

@Local
public interface SurveyService {

    void saveVote(SurveyOption surveyOption) throws Exception;
}
