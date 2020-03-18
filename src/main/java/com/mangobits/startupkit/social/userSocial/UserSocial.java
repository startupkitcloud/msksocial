package com.mangobits.startupkit.social.userSocial;

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
@Entity(name="userSocial")
@Indexed
public class UserSocial {



    @Id
    @DocumentId
    private String id;


    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<String> listFavorites;

    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<String> listGroups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getListFavorites() {
        return listFavorites;
    }

    public void setListFavorites(List<String> listFavorites) {
        this.listFavorites = listFavorites;
    }

    public List<String> getListGroups() {
        return listGroups;
    }

    public void setListGroups(List<String> listGroups) {
        this.listGroups = listGroups;
    }

    public UserSocial(){
    }

    public UserSocial(String id){
        this.id = id;
    }
}
