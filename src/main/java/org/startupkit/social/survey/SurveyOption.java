package org.startupkit.social.survey;

import org.bson.codecs.pojo.annotations.BsonIgnore;

public class SurveyOption {

    public SurveyOption() {
    }

    private String id;

    private String title;

    @BsonIgnore
    private String idPost;

    private Double numberOfVotes;

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
}
