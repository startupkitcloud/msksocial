package com.mangobits.startupkit.social.groupInfo;

import com.mangobits.startupkit.social.group.UserGroup;

import javax.ejb.Local;
import java.util.List;

@Local
public interface GroupInfoService {

    GroupInfo retrieve(String idGroup) throws Exception;

    List<UserGroup> listActiveUsers(String idGroup) throws Exception;

}
