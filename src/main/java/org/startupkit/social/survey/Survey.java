package org.startupkit.social.survey;

import java.util.Date;
import java.util.List;

public class Survey {

    private Integer totalVotes;

    private Date creationDate;

    private List<SurveyOption> listSurveyOptions;

    private List<String> listUsers;

    public Survey() {
    }


    public List<SurveyOption> getListSurveyOptions() {
        return listSurveyOptions;
    }

    public void setListSurveyOptions(List<SurveyOption> listSurveyOptions) {
        this.listSurveyOptions = listSurveyOptions;
    }


    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }


    public List<String> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<String> listUsers) {
        this.listUsers = listUsers;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
