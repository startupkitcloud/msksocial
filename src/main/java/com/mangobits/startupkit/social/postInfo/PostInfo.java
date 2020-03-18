package com.mangobits.startupkit.social.postInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.social.comment.Comment;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="postInfo")
@Indexed
public class PostInfo {


    // idPost
    @Id
    @DocumentId
    private String id;


    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<Comment> listActiveComments;

    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<Comment> listBlockedComments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public List<Comment> getListBlockedComments() {
        return listBlockedComments;
    }

    public void setListBlockedComments(List<Comment> listBlockedComments) {
        this.listBlockedComments = listBlockedComments;
    }

    public List<Comment> getListActiveComments() {
        return listActiveComments;
    }

    public void setListActiveComments(List<Comment> listActiveComments) {
        this.listActiveComments = listActiveComments;
    }

    public PostInfo(){
    }

    public PostInfo(String id){
        this.id = id;
    }

}
