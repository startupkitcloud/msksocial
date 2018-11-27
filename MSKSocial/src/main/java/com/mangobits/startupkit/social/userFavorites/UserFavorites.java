package com.mangobits.startupkit.social.userFavorites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.social.post.Post;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name="userFavorites")
@Indexed
public class UserFavorites {


    // idUser
    @Id
    @DocumentId
    private String id;


    @IndexedEmbedded
    @ElementCollection(fetch= FetchType.EAGER)
    private List<String> listFavorites;


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

    public UserFavorites(){
    }

    public UserFavorites(String id){
        this.id = id;
    }

}
