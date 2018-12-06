package com.mangobits.startupkit.social.group;

import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.core.utils.BusinessUtils;
import com.mangobits.startupkit.notification.NotificationBuilder;
import com.mangobits.startupkit.notification.NotificationService;
import com.mangobits.startupkit.notification.TypeSendingNotificationEnum;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.groupInfo.GroupInfo;
import com.mangobits.startupkit.social.groupInfo.GroupInfoDAO;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GroupServiceImpl implements GroupService {

    private static final int TOTAL_PAGE = 10;


    @New
    @Inject
    private GroupDAO groupDAO;

    @New
    @Inject
    private GroupInfoDAO groupInfoDAO;

    @EJB
    private UserService userService;

    @EJB
    private NotificationService notificationService;

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

        GroupInfo groupInfo = groupInfoDAO.retrieve(new GroupInfo(idGroup));
        if (groupInfo == null || groupInfo.getListUsers() == null) {
            group.setNumberOfMembers(0);
        }else {
            group.setNumberOfMembers(groupInfo.getListUsers().size());
        }

        return group;
    }

    @Override
    public Group save (Group group, User user) throws Exception {


        if (user.getStatus() == UserStatusEnum.BLOCKED){
            throw new BusinessException("user_blocked");
        }


        if(group.getId() == null){

            group.setCreationDate(new Date());
            group.setStatus(SimpleStatusEnum.ACTIVE);

            groupDAO.insert(group);

//            group.setListUsers(new ArrayList<>());

            UserGroup userGroup = new UserGroup();
            userGroup.setIdUser(user.getId());
            userGroup.setFgAdmin(true);
            userGroup.setIdGroup(group.getId());
            userGroup.setNameUser(user.getName());

            // adiciona o user no groupInfo
            GroupInfo groupInfo = groupInfoDAO.retrieve(new GroupInfo(userGroup.getIdGroup()));
            if (groupInfo == null){
                groupInfo = new GroupInfo();
                groupInfo.setId(group.getId());
            }
            if (groupInfo.getListUsers() == null){
                groupInfo.setListUsers(new ArrayList<>());
            }
            groupInfo.getListUsers().add(userGroup);
            groupInfoDAO.insert(groupInfo);


        }else {

            GroupInfo groupInfo = groupInfoDAO.retrieve(new GroupInfo(group.getId()));
            if (groupInfo == null) {
                throw new BusinessException("groupInfo_not_found");
            }

            UserGroup userGroupBaseAdmin = groupInfo.getListUsers().stream()
                    .filter(p -> p.getIdUser().equals(user.getId()))
                    .findFirst()
                    .orElse(null);

            if (userGroupBaseAdmin == null){
                throw new BusinessException("user_not_found_inside_group");
            }
            if (!userGroupBaseAdmin.getFgAdmin()){
                throw new BusinessException("user_not_admin");
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


        // adiciona o user no groupInfo
        GroupInfo groupInfo = groupInfoDAO.retrieve(new GroupInfo(userGroup.getIdGroup()));

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


        sendNotification(user, group.getTitle(), group.getId(), "Você foi adicionado no grupo", "GROUP", group.getCategory());

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

        GroupInfo groupInfo = groupInfoDAO.retrieve(new GroupInfo(userGroup.getIdGroup()));
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

        sendNotification(user, group.getTitle(), group.getId(), "Você saiu do grupo", "GROUP", group.getCategory());

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


        if (groupSearch.getIdUser() != null) {
            list = listByUser(groupSearch.getIdUser());
        }
        return list;
    }

    @Override
    public List<Group> listByUser (String idUser) throws Exception {

        SearchBuilder searchBuilder = new SearchBuilder();
        searchBuilder.appendParam("status", SimpleStatusEnum.ACTIVE);

        //ordena
        List<Group> list = this.groupDAO.search(searchBuilder.build());

//        List<Group> listUserGroups = new ArrayList<>();
//
//        for (Group group: list){
//
//            UserGroup user = group.getListUsers().stream()
//                    .filter(p -> p.getIdUser().equals(idUser))
//                    .findFirst()
//                    .orElse(null);
//
//            if (user != null){
//                listUserGroups.add(group);
//            }
//
//            list = listUserGroups;
//        }
        return list;
    }


    private void sendNotification(User user, String title, String link, String msg, String type, String info) throws Exception {
        notificationService.sendNotification(new NotificationBuilder()
                .setTo(user)
                .setTypeSending(TypeSendingNotificationEnum.APP)
                .setTypeFrom(type)
                .setTitle(title)
                .setIdLink(link)
                .setMessage(msg)
                .setInfo(info)
                .build());
    }

}

