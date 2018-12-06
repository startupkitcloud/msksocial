package com.mangobits.startupkit.social.groupInfo;

import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.group.UserGroup;


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
}
