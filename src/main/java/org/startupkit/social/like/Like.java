package org.startupkit.social.like;


import java.util.Date;


public class Like {

    private String idObjectLiked;

    private String typeObjectLiked;

    private String idObjectLiker;

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
