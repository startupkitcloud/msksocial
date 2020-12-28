package org.startupkit.social.groupInfo;

import org.startupkit.social.group.UserGroup;
import org.startupkit.user.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface GroupInfoService {

    GroupInfo retrieve(String idGroup) throws Exception;

    List<UserGroup> listActiveUsers(String idGroup) throws Exception;

    void addUser(UserGroup userGroup, User userAdmin) throws Exception;

    void removeUser(UserGroup userGroup, User userAdmin) throws Exception;
}
