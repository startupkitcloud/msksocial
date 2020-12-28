package org.startupkit.social.survey;

import org.startupkit.social.post.Post;
import org.startupkit.user.User;

import javax.ejb.Local;

@Local
public interface SurveyService {

    Post saveVote(SurveyOption surveyOption, User user) throws Exception;

}
