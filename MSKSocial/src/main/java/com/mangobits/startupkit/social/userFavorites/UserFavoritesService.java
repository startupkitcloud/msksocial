package com.mangobits.startupkit.social.userFavorites;

import javax.ejb.Local;

@Local
public interface UserFavoritesService {

    Boolean favoritePost(String idPost, String idUser) throws Exception;

    UserFavorites retrieve(String idUser) throws Exception;

    UserFavorites load (String idUser) throws Exception;
}
