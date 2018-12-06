package com.mangobits.startupkit.social.userSocial;

import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.social.group.Group;
import com.mangobits.startupkit.social.group.GroupService;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostService;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserSocialServiceImpl implements UserSocialService {

    @Inject
    @New
    private UserSocialDAO userSocialDAO;

    @EJB
    private PostService postService;

    @EJB
    private GroupService groupService;

    @Override
    public UserSocial retrieve(String idUser) throws Exception {

        UserSocial userSocial =  userSocialDAO.retrieve(new UserSocial(idUser));

        return userSocial;
    }

    @Override
    public Boolean favoritePost(String idPost, String idUser) throws Exception {

        Boolean remove = false;
        UserSocial userSocial = retrieve(idUser);
        Post post = postService.retrieve(idPost);
        if (post == null){
            throw new BusinessException("post_not_found");
        }

        if (userSocial == null){
            userSocial = new UserSocial();
            userSocial.setId(idUser);
            userSocial.setListFavorites(new ArrayList<>());
            userSocial.getListFavorites().add(idPost);
            userSocialDAO.insert(userSocial);
        }else {
            if (userSocial.getListFavorites() == null){
                userSocial.setListFavorites(new ArrayList<>());
                userSocial.getListFavorites().add(idPost);
            }else {

                String postBase = userSocial.getListFavorites().stream()
                        .filter(p -> p.equals(idPost))
                        .findFirst()
                        .orElse(null);

                if (postBase != null){
                    userSocial.getListFavorites().remove(postBase);
                    remove = true;
                }else {
                    userSocial.getListFavorites().add(idPost);
                }
            }

            userSocialDAO.update(userSocial);
        }

        return remove;

    }

    @Override
    public void addGroup(String idGroup, String idUser) throws Exception {

        UserSocial userSocial = retrieve(idUser);
        Group group = groupService.load(idGroup);
        if (group == null){
            throw new BusinessException("group_not_found");
        }

        if (userSocial == null){
            userSocial = new UserSocial();
            userSocial.setId(idUser);
            userSocial.setListGroups(new ArrayList<>());
            userSocial.getListGroups().add(idGroup);
            userSocialDAO.insert(userSocial);
        }else {
            if (userSocial.getListGroups() == null){
                userSocial.setListGroups(new ArrayList<>());
                userSocial.getListGroups().add(idGroup);
            }else {

                String groupBase = userSocial.getListGroups().stream()
                        .filter(p -> p.equals(idGroup))
                        .findFirst()
                        .orElse(null);

                if (groupBase != null){
                   throw new BusinessException("group_exists_on_listGroups");
                }else {
                    userSocial.getListGroups().add(idGroup);
                }
            }

            userSocialDAO.update(userSocial);
        }

    }

    @Override
    public void removeGroup(String idGroup, String idUser) throws Exception {

        UserSocial userSocial = retrieve(idUser);
        Group group = groupService.load(idGroup);
        if (group == null){
            throw new BusinessException("group_not_found");
        }

        if (userSocial == null){
           throw new BusinessException("userSocial_not_found");
        }

        if (userSocial.getListGroups() == null){
               throw new BusinessException("listGroups_not_found");
        }

        String groupBase = userSocial.getListGroups().stream()
                .filter(p -> p.equals(idGroup))
                .findFirst()
                .orElse(null);

        if (groupBase == null){
            throw new BusinessException("group_not_found_on_listGroups");
        }

        userSocial.getListGroups().remove(idGroup);
        userSocialDAO.update(userSocial);

    }


}
