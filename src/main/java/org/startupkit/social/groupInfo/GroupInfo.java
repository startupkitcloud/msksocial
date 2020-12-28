package org.startupkit.social.groupInfo;

import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;
import org.startupkit.social.group.UserGroup;

import java.util.List;

@MSKEntity(name="groupInfo")
public class GroupInfo {


    @MSKId
    private String id;

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
