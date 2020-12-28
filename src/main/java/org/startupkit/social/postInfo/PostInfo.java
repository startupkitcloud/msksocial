package org.startupkit.social.postInfo;

import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;
import org.startupkit.social.comment.Comment;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

@MSKEntity(name="postInfo")
public class PostInfo {


    @MSKId
    private String id;

    private List<Comment> listActiveComments;

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
