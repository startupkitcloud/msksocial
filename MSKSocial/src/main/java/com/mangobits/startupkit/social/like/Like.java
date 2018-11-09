package com.mangobits.startupkit.social.like;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Indexed
@Embeddable
@MappedSuperclass
public class Like {

    @Field
    private String idObjectLiked;


    @Field
    private String typeObjectLiked;


    @Field
    private String idObjectLiker;


    @Field
    private String typeObjectLiker;




    private String nameObjectLiker;

    private String nameObjectLiked;


    private Date creationDate;


    public String getIdObjectLiked() {
        return idObjectLiked;
    }

    public void setIdObjectLiked(String idObject) {
        this.idObjectLiked = idObject;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getTypeObjectLiked() {
        return typeObjectLiked;
    }

    public void setTypeObjectLiked(String typeObject) {
        this.typeObjectLiked = typeObject;
    }

    public String getIdObjectLiker() {
        return idObjectLiker;
    }

    public void setIdObjectLiker(String idObjectLiker) {
        this.idObjectLiker = idObjectLiker;
    }

    public String getTypeObjectLiker() {
        return typeObjectLiker;
    }

    public void setTypeObjectLiker(String typeObjectLiker) {
        this.typeObjectLiker = typeObjectLiker;
    }

    public String getNameObjectLiker() {
        return nameObjectLiker;
    }

    public void setNameObjectLiker(String nameObjectLiker) {
        this.nameObjectLiker = nameObjectLiker;
    }

    public String getNameObjectLiked() {
        return nameObjectLiked;
    }

    public void setNameObjectLiked(String nameObjectLiked) {
        this.nameObjectLiked = nameObjectLiked;
    }
}
