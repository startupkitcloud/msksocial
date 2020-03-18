package com.mangobits.startupkit.social.groupInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.social.group.UserGroup;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="groupInfo")
@Indexed
public class GroupInfo {


    @Id
    @DocumentId
    private String id;


    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<UserGroup> listUsers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<UserGroup> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<UserGroup> listUsers) {
        this.listUsers = listUsers;
    }

    public GroupInfo(){
    }

    public GroupInfo(String id){
        this.id = id;
    }


}
