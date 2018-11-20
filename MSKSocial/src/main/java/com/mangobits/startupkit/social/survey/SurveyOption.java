package com.mangobits.startupkit.social.survey;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Date;

@Embeddable
@Indexed
public class SurveyOption {

    public SurveyOption() {
    }



    private String id;

    private String title;


    @Transient
    private String idPost;

    @Transient
    private String idUser;

    @Field
    private Double numberOfVotes;

    @Field
    private Double percentageOfVotes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(Double numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public Double getPercentageOfVotes() {
        return percentageOfVotes;
    }

    public void setPercentageOfVotes(Double percentageOfVotes) {
        this.percentageOfVotes = percentageOfVotes;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


}
