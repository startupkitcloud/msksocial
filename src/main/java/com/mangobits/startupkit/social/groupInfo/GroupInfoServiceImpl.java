package com.mangobits.startupkit.social.groupInfo;

import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.group.UserGroup;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserService;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GroupInfoServiceImpl implements GroupInfoService {

    @New
    @Inject
    private GroupInfoDAO groupInfoDAO;

    @EJB
    private UserService userService;

    @Override
    public GroupInfo retrieve(String idGroup) throws Exception {

        GroupInfo groupInfo =  groupInfoDAO.retrieve(new GroupInfo(idGroup));

        return groupInfo;
    }

    @Override
    public List<UserGroup> listActiveUsers(String idGroup) throws Exception {

        List<UserGroup> list = groupInfoDAO.listActiveUsers(idGroup);
        return list;

    }

    @Override
    public void addUser(UserGroup userGroup, User userAdmin) throws Exception {

        GroupInfo groupInfo = retrieve(userGroup.getIdGroup());
        if (groupInfo == null){
            throw new BusinessException("groupInfo_not_found");
        }


        User user = userService.retrieve(userGroup.getIdUser());
        if (user == null){
            throw new BusinessException("user_not_found");
        }

        UserGroup userGroupBaseAdmin = groupInfo.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userAdmin.getId()))
                .findFirst()
                .orElse(null);

        if (userGroupBaseAdmin == null){
            throw new BusinessException("userAdmin_not_found");
        }
        if (!userGroupBaseAdmin.getFgAdmin()){
            throw new BusinessException("userAdmin_not_admin");
        }

        UserGroup userGroupBase = groupInfo.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userGroup.getIdUser()))
                .findFirst()
                .orElse(null);

        if (userGroupBase != null){
            throw new BusinessException("user_already_member");
        }

        if (groupInfo.getListUsers() == null){
            groupInfo.setListUsers(new ArrayList<>());
        }
        if (userGroup.getNameUser() == null && user.getName() != null){
            userGroup.setNameUser(user.getName());
        }
        groupInfo.getListUsers().add(userGroup);
        groupInfoDAO.update(groupInfo);

    }

    @Override
    public void removeUser(UserGroup userGroup, User userAdmin) throws Exception {

        GroupInfo groupInfo = retrieve(userGroup.getIdGroup());
        if (groupInfo == null){
            throw new BusinessException("groupInfo_not_found");
        }

        if (groupInfo.getListUsers() == null){
            throw new BusinessException("groupInfo_empty");
        }

        UserGroup userGroupBaseAdmin = groupInfo.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userAdmin.getId()))
                .findFirst()
                .orElse(null);

        if (userGroupBaseAdmin == null){
            throw new BusinessException("userAdmin_not_found");
        }
        if (!userGroupBaseAdmin.getFgAdmin()){
            throw new BusinessException("userAdmin_not_admin");
        }

        UserGroup userGroupBase = groupInfo.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userGroup.getIdUser()))
                .findFirst()
                .orElse(null);

        if (userGroupBase == null){
            throw new BusinessException("user_not_member");
        }
        groupInfo.getListUsers().remove(userGroupBase);

        groupInfoDAO.update(groupInfo);


    }
}
