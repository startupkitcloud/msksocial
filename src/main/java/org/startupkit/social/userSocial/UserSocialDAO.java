package org.startupkit.social.userSocial;

import org.startupkit.core.dao.AbstractDAO;

public class UserSocialDAO extends AbstractDAO<UserSocial> {

    public UserSocialDAO() {
        super(UserSocial.class);
    }


    @Override
    public Object getId(UserSocial obj) {
        return obj.getId();
    }
}
