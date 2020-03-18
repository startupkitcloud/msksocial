package com.mangobits.startupkit.social.group;

import com.mangobits.startupkit.user.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface GroupService {

    Group save (Group group, User user) throws Exception;

  void addUser(UserGroup userGroup, User userAdmin) throws Exception;

     void removeUser(UserGroup userGroup, User userAdmin) throws Exception;

    Group load (String idGroup) throws Exception;

    List<Group> listAll() throws Exception;

    String pathFilesGroup(String var1) throws Exception;

    List<Group> search(GroupSearch groupSearch) throws Exception;

    List<Group> listByUser (GroupSearch groupSearch) throws Exception;
}
