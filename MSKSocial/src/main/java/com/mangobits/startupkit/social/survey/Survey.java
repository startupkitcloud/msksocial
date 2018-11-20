package com.mangobits.startupkit.social.survey;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.List;

@Embeddable
@Indexed
public class Survey {

    public Survey() {
    }


    private Integer totalVotes;

    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<SurveyOption> listSurveyOptions;

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

}
