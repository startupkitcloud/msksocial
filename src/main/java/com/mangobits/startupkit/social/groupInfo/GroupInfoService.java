package com.mangobits.startupkit.social.groupInfo;

import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.social.group.Group;
import com.mangobits.startupkit.social.group.UserGroup;
import com.mangobits.startupkit.social.userSocial.UserSocial;
import com.mangobits.startupkit.user.User;

import javax.ejb.Local;
import java.util.ArrayList;
import java.util.List;

@Local
public interface GroupInfoService {

    GroupInfo retrieve(String idGroup) throws Exception;

    List<UserGroup> listActiveUsers(String idGroup) throws Exception;

    void addUser(UserGroup userGroup, User userAdmin) throws Exception;

    void removeUser(UserGroup userGroup, User userAdmin) throws Exception;


}
