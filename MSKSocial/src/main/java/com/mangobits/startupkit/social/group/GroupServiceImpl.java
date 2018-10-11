package com.mangobits.startupkit.social.group;

import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.core.utils.BusinessUtils;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.like.LikesService;
import com.mangobits.startupkit.social.post.*;
import com.mangobits.startupkit.social.postInfo.PostInfo;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserService;
import com.mangobits.startupkit.user.UserStatusEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GroupServiceImpl implements GroupService {

    private static final int TOTAL_PAGE = 10;


    @New
    @Inject
    private GroupDAO groupDAO;

    @EJB
    private UserService userService;

    @EJB
    private ConfigurationService configurationService;


    private Group retrieve(String idGroup) throws Exception {

        Group group =  groupDAO.retrieve(new Group(idGroup));

        return group;
    }

    @Override
    public Group load (String idGroup) throws Exception {

        Group group =  retrieve(idGroup);

        if (group == null){
            throw new BusinessException("group_not_found");
        }
        group.setNumberOfMembers(group.getListUsers().size());

        return group;
    }

    @Override
    public Group save (Group group, User userAdmin) throws Exception {


        if (userAdmin.getStatus() == UserStatusEnum.BLOCKED){
            throw new BusinessException("user_blocked");
        }

        if(group.getStatus() == null){
            group.setStatus(SimpleStatusEnum.ACTIVE);
        }

        if(group.getId() == null){

            group.setCreationDate(new Date());
            groupDAO.insert(group);

            group.setListUsers(new ArrayList<>());

            UserGroup userGroup = new UserGroup();
            userGroup.setIdUser(userAdmin.getId());
            userGroup.setFgAdmin(true);
            userGroup.setIdGroup(group.getId());
            userGroup.setNameUser(userAdmin.getName());
            group.getListUsers().add(userGroup);

            groupDAO.update(group);

        }else {

            if (group.getListUsers().size() == 0){
                UserGroup userGroup = new UserGroup();
                userGroup.setIdUser(userAdmin.getId());
                userGroup.setFgAdmin(true);
                userGroup.setIdGroup(group.getId());
                userGroup.setNameUser(userAdmin.getName());
                group.getListUsers().add(userGroup);
            }
            new BusinessUtils<>(groupDAO).basicSave(group);
        }

        return group;

    }

    @Override
    public void addUser(UserGroup userGroup, User userAdmin) throws Exception {


        if(userGroup.getIdGroup() == null){
            throw new BusinessException("missing_idGroup");
        }

        if(userGroup.getIdUser() == null){
            throw new BusinessException("missing_idUser");
        }

        if(userGroup.getFgAdmin() == null){
            throw new BusinessException("missing_fgAdmin");
        }

        User user = userService.retrieve(userGroup.getIdUser());
        if (user == null){
            throw new BusinessException("user_not_found");
        }
        if (user.getStatus() == UserStatusEnum.BLOCKED){
            throw new BusinessException("user_blocked");
        }

        Group group = retrieve(userGroup.getIdGroup());

        if (group.getListUsers() == null){
            throw new BusinessException("no_userAdmin_on_group");
        }

        UserGroup userGroupBaseAdmin = group.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userAdmin.getId()))
                .findFirst()
                .orElse(null);

        if (userGroupBaseAdmin == null){
            throw new BusinessException("userAdmin_not_found");
        }
        if (!userGroupBaseAdmin.getFgAdmin()){
            throw new BusinessException("userAdmin_not_admin");
        }


        UserGroup userGroupBase = group.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userGroup.getIdUser()))
                .findFirst()
                .orElse(null);

        if (userGroupBase != null){
            throw new BusinessException("user_already_member");
        }
        group.getListUsers().add(userGroup);
        groupDAO.update(group);

    }

    @Override
    public void removeUser(UserGroup userGroup, User userAdmin) throws Exception {


        if(userGroup.getIdGroup() == null){
            throw new BusinessException("missing_idGroup");
        }

        if(userGroup.getIdUser() == null){
            throw new BusinessException("missing_idUser");
        }
        User user = userService.retrieve(userGroup.getIdUser());
        if (user == null){
            throw new BusinessException("user_not_found");
        }

        Group group = retrieve(userGroup.getIdGroup());
        if (group == null){
            throw new BusinessException("group_not_found");
        }

        if (group.getListUsers() == null){
           throw new BusinessException("group_empty");
        }

        UserGroup userGroupBaseAdmin = group.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userAdmin.getId()))
                .findFirst()
                .orElse(null);

        if (userGroupBaseAdmin == null){
            throw new BusinessException("userAdmin_not_found");
        }
        if (!userGroupBaseAdmin.getFgAdmin()){
            throw new BusinessException("userAdmin_not_admin");
        }

        UserGroup userGroupBase = group.getListUsers().stream()
                .filter(p -> p.getIdUser().equals(userGroup.getIdUser()))
                .findFirst()
                .orElse(null);

        if (userGroupBase == null){
            throw new BusinessException("user_not_member");
        }
        group.getListUsers().remove(userGroupBase);

        groupDAO.update(group);

    }

    @Override
    public List<Group> listAll() throws Exception {

        List<Group> list = this.groupDAO.search((new SearchBuilder()).appendParam("status", SimpleStatusEnum.ACTIVE).build());

        return list;
    }

    @Override
    public String pathFilesGroup(String idGroup) throws Exception {
        return this.configurationService.loadByCode(ConfigurationEnum.PATH_BASE).getValue() + "/group/" + idGroup;
    }

    @Override
    public List<Group> search(GroupSearch groupSearch) throws Exception {

        SearchBuilder searchBuilder = new SearchBuilder();
        searchBuilder.appendParam("status", SimpleStatusEnum.ACTIVE);
        if (groupSearch.getQueryString() != null && StringUtils.isNotEmpty(groupSearch.getQueryString().trim())) {
            searchBuilder.appendParam("title|desc|", groupSearch.getQueryString());
        }
        searchBuilder.setFirst(TOTAL_PAGE * (groupSearch.getPage() -1));
        searchBuilder.setMaxResults(TOTAL_PAGE);
        Sort sort = new Sort(new SortField("creationDate", SortField.Type.LONG, true));
        searchBuilder.setSort(sort);

        //ordena
        List<Group> list = this.groupDAO.search(searchBuilder.build());

        return list;
    }


}

