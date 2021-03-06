package org.startupkit.social.comment;

import org.startupkit.core.status.SimpleStatusEnum;

import javax.json.bind.annotation.JsonbDateFormat;
import java.util.Date;

public class Comment {

    private String id;

    private String text;

    private String idUser;

    private String nameUser;

    private String idPost;

    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    private SimpleStatusEnum status;

    public SimpleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SimpleStatusEnum status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public Comment() {
    }

    public Comment(String id) {
        this.id = id;
    }

}
