package org.startupkit.social.userSocial;

import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;

import java.util.List;

@MSKEntity(name="userSocial")
public class UserSocial {

    @MSKId
    private String id;

    private List<String> listFavorites;

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
