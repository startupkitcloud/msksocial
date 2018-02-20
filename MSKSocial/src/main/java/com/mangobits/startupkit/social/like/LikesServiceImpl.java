package com.mangobits.startupkit.social.like;


import com.mangobits.startupkit.core.exception.ApplicationException;
import com.mangobits.startupkit.core.exception.BusinessException;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LikesServiceImpl implements LikesService {


    @Inject
    @New
    private LikesDAO likesDAO;



    @Override
    public void like(Like like) throws ApplicationException, BusinessException {

        try {

            //trata o objeto que foi curtido
            Likes likesLiked = likesDAO.retrieve(new Likes(like.getIdObjectLiked()));
            boolean insertLiked = false;

            if(likesLiked == null){
                likesLiked = new Likes();
                likesLiked.setIdOjbect(like.getIdObjectLiked());
                likesLiked.setTypeObject(like.getTypeObjectLiked());
            }

            if(likesLiked.getListLikesMe() == null){
                likesLiked.setListLikesMe(new ArrayList<>());
            }

            Like likedDB = likesLiked.getListLikesMe().stream()
                    .filter(p -> p.getIdObjectLiker().equals(like.getIdObjectLiker()))
                    .findFirst()
                    .orElse(null);

            if(likedDB == null){
                likesLiked.getListLikesMe().add(like);
            }
            else{
                likesLiked.getListLikesMe().remove(likedDB);
            }

            if(insertLiked){
                likesDAO.insert(likesLiked);
            }
            else{
                likesDAO.update(likesLiked);
            }


            //trata o objeto que curtiu
            Likes likesLiker = likesDAO.retrieve(new Likes(like.getIdObjectLiker()));
            boolean insertLiker = false;

            if(likesLiker == null){
                likesLiker = new Likes();
                likesLiker.setIdOjbect(like.getIdObjectLiker());
                likesLiker.setTypeObject(like.getTypeObjectLiker());
            }

            if(likesLiker.getListILike() == null){
                likesLiker.setListILike(new ArrayList<>());
            }

            Like likerDB = likesLiker.getListILike().stream()
                    .filter(p -> p.getIdObjectLiked().equals(like.getIdObjectLiked()))
                    .findFirst()
                    .orElse(null);

            if(likerDB == null){
                likesLiker.getListILike().add(like);
            }
            else{
                likesLiker.getListILike().remove(likerDB);
            }

            if(insertLiker){
                likesDAO.insert(likesLiker);
            }
            else{
                likesDAO.update(likesLiker);
            }
        }
        catch (Exception e){
            throw new ApplicationException("got an error executing a like", e);
        }
    }



    @Override
    public List<? extends Like> listLikesMe(String idObject) throws BusinessException, ApplicationException {

        List<Like> list = new ArrayList<>();

        try {

            Likes likes = likesDAO.retrieve(new Likes(idObject));

            if(likes != null){
                list = likes.getListLikesMe();
            }
        }
        catch(Exception e){
            throw new ApplicationException("got an error listing likes me", e);
        }

        return list;
    }




    @Override
    public List<? extends Like> listILike(String idObject) throws BusinessException, ApplicationException {

        List<? extends Like> list = new ArrayList<>();

        try {

            Likes likes = likesDAO.retrieve(new Likes(idObject));

            if(likes != null){
                list = likes.getListILike();
            }
        }
        catch(Exception e){
            throw new ApplicationException("got an error listing I like", e);
        }

        return list;
    }
}
