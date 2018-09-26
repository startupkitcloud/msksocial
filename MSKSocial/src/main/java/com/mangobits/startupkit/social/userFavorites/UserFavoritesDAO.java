package com.mangobits.startupkit.social.userFavorites;

import com.mangobits.startupkit.core.dao.AbstractDAO;

public class UserFavoritesDAO extends AbstractDAO<UserFavorites> {

    public UserFavoritesDAO(){
        super(UserFavorites.class);
    }


    @Override
    public Object getId(UserFavorites obj) {
        return obj.getId();
    }
}
