package com.mangobits.startupkit.social.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Embeddable
@Indexed
public class Survey {

    public Survey() {
    }




    private Integer totalVotes;



    private Date creationDate;


    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<SurveyOption> listSurveyOptions;



    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<String> listUsers;

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
