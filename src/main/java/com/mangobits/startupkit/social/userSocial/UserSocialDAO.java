package com.mangobits.startupkit.social.userSocial;

import com.mangobits.startupkit.core.dao.AbstractDAO;
import com.mangobits.startupkit.social.groupInfo.GroupInfo;

public class UserSocialDAO extends AbstractDAO<UserSocial> {

    public UserSocialDAO() {
        super(UserSocial.class);
    }


    @Override
    public Object getId(UserSocial obj) {
        return obj.getId();
    }
}
