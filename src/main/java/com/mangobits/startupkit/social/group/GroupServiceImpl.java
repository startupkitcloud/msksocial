package com.mangobits.startupkit.social.group;

import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.dao.OperationEnum;
import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.core.utils.BusinessUtils;
import com.mangobits.startupkit.notification.NotificationBuilder;
import com.mangobits.startupkit.notification.NotificationService;
import com.mangobits.startupkit.notification.TypeSendingNotificationEnum;
import com.mangobits.startupkit.social.groupInfo.GroupInfo;
import com.mangobits.startupkit.social.groupInfo.GroupInfoDAO;
import com.mangobits.startupkit.social.groupInfo.GroupInfoService;

import com.mangobits.startupkit.social.userSocial.UserSocial;
import com.mangobits.startupkit.social.userSocial.UserSocialService;
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
    private UserSocialService userSocialService;

    @EJB
    private GroupInfoService groupInfoService;

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
            GroupInfo groupInfo = groupInfoService.retrieve(userGroup.getIdGroup());
            if (groupInfo == null){
                groupInfo = new GroupInfo();
                groupInfo.setId(group.getId());
            }
            if (groupInfo.getListUsers() == null){
                groupInfo.setListUsers(new ArrayList<>());
            }
            groupInfo.getListUsers().add(userGroup);
            groupInfoDAO.insert(groupInfo);

            // adiciona o idGroup no listGroups do userSocial
            userSocialService.addGroup(group.getId(), userGroup.getIdUser());

            // atualiza o numberOfMembers do group
            int numberOfMembers = 0;
            if (group.getNumberOfMembers() != null){
                numberOfMembers = group.getNumberOfMembers();
            }
            group.setNumberOfMembers(numberOfMembers + 1);
            new BusinessUtils<>(groupDAO).basicSave(group);


        }else {

            GroupInfo groupInfo = groupInfoService.retrieve(group.getId());
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
        if (group == null){
            throw new BusinessException("group_not_found");
        }

        // adiciona o user no listUsers do groupInfo
        groupInfoService.addUser(userGroup, userAdmin);


        // adiciona o idGroup no listGroups do userSocial
        userSocialService.addGroup(group.getId(), userGroup.getIdUser());

        // atualiza o numberOfMembers do group
        group.setNumberOfMembers(group.getNumberOfMembers() + 1);
        new BusinessUtils<>(groupDAO).basicSave(group);

        // envia notificação
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
        if (group == null){
            throw new BusinessException("group_not_found");
        }

       // remove o user da lista de Users do GroupInfo
        groupInfoService.removeUser(userGroup, userAdmin);

        // remove o idGroup do listGroups do userSocial
        userSocialService.removeGroup(group.getId(), userGroup.getIdUser());

        // atualiza o numberOfMembers do group
        group.setNumberOfMembers(group.getNumberOfMembers() - 1);
        new BusinessUtils<>(groupDAO).basicSave(group);


        // envia notificacao
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
            searchBuilder.setFirst(TOTAL_PAGE * (groupSearch.getPage() - 1));
            searchBuilder.setMaxResults(TOTAL_PAGE);
            Sort sort = new Sort(new SortField("creationDate", SortField.Type.LONG, true));
            searchBuilder.setSort(sort);

            //ordena
            List<Group> list = this.groupDAO.search(searchBuilder.build());


        return list;
    }



    @Override
    public List<Group> listByUser (GroupSearch groupSearch) throws Exception {

        if (groupSearch.getIdUser() == null){
            throw new BusinessException("missing_idUser");
        }

        List<Group> list = new ArrayList<>();

        UserSocial userSocial = userSocialService.retrieve(groupSearch.getIdUser());

        if (userSocial != null && userSocial.getListGroups() != null && userSocial.getListGroups().size() > 0){
            List<String> listIds = userSocial.getListGroups();
            Sort sort = new Sort(new SortField("creationDate", SortField.Type.LONG, true));

            SearchBuilder sb = groupDAO.createBuilder()
                            .appendParamQuery("id", listIds, OperationEnum.IN)
                            .setSort(sort);

            if(groupSearch.getQueryString() != null){
               sb.appendParamQuery("title", groupSearch.getQueryString(), OperationEnum.LIKE);
            }

            list = groupDAO.search(sb.build());
        }


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
