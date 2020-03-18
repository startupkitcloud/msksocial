package com.mangobits.startupkit.social.userSocial;

import com.mangobits.startupkit.social.groupInfo.GroupInfo;

import javax.ejb.Local;

@Local
public interface UserSocialService {

    Boolean favoritePost(String idPost, String idUser) throws Exception;

    UserSocial retrieve(String idUser) throws Exception;

    void removeGroup(String idGroup, String idUser) throws Exception;

    void addGroup(String idGroup, String idUser) throws Exception;

}
