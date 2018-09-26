package com.mangobits.startupkit.social.userFavorites;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserFavoritesServiceImpl implements UserFavoritesService {

    @Inject
    @New
    private UserFavoritesDAO userFavoritesDAO;



    @Override
    public Boolean favoritePost(String idPost, String idUser) throws Exception {

        Boolean remove = false;
        UserFavorites userFavorites = retrieve(idUser);

        if (userFavorites == null){
            userFavorites = new UserFavorites();
            userFavorites.setId(idUser);
            userFavorites.setListFavorites(new ArrayList<>());
            userFavorites.getListFavorites().add(idPost);
            userFavoritesDAO.insert(userFavorites);
        }else {
            if (userFavorites.getListFavorites() == null){
                userFavorites.setListFavorites(new ArrayList<>());
                userFavorites.getListFavorites().add(idPost);
            }else {

                String idPostBase = userFavorites.getListFavorites().stream()
                        .filter(p -> p.equals(idPost))
                        .findFirst()
                        .orElse(null);

                if (idPostBase != null){
                    userFavorites.getListFavorites().remove(idPost);
                    remove = true;
                }else {
                    userFavorites.getListFavorites().add(idPost);
                }
            }

            userFavoritesDAO.update(userFavorites);
        }

        return remove;

    }


    @Override
    public UserFavorites retrieve(String idUser) throws Exception {

        UserFavorites userFavorites =  userFavoritesDAO.retrieve(new UserFavorites(idUser));

        return userFavorites;
    }

    @Override
    public UserFavorites load (String idUser) throws Exception {

        UserFavorites userFavorites = retrieve(idUser);

        return userFavorites;
    }


}
