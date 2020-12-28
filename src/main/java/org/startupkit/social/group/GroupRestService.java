package org.startupkit.social.group;

import org.startupkit.core.configuration.Configuration;
import org.startupkit.core.configuration.ConfigurationEnum;
import org.startupkit.core.configuration.ConfigurationService;
import org.startupkit.core.exception.BusinessException;
import org.startupkit.core.photo.PhotoUpload;
import org.startupkit.core.photo.PhotoUtils;
import org.startupkit.core.utils.FileUtil;
import org.startupkit.social.groupInfo.GroupInfoService;
import org.startupkit.user.User;
import org.startupkit.user.util.SecuredUser;
import org.startupkit.user.util.UserBaseRestService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Stateless
@Path("/group")
public class GroupRestService extends UserBaseRestService {

    @EJB
    private GroupService groupService;

    @EJB
    private GroupInfoService groupInfoService;

    @EJB
    private ConfigurationService configurationService;

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/save")
    public Group save(Group group)  throws Exception{
        User user = super.getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_not_found");
        }
        return groupService.save(group, user);
    }


    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addUser")
    public void addUser(UserGroup userGroup)  throws Exception{

        User user = super.getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_admin_not_found");
        }
        groupService.addUser(userGroup, user);
    }


    @SecuredUser
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeUser")
    public void removeUser(UserGroup userGroup)  throws Exception{

        User user = super.getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_admin_not_found");
        }
        groupService.removeUser(userGroup, user);
    }


    @GET
    @Path("/listAll")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Group> listAll() throws Exception {
        return groupService.listAll();
    }


    @GET
    @Path("/load/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Group load(@PathParam("id") String id) throws Exception {
        return groupService.load(id);
    }


    @GET
    @Path("/groupImage/{idGroup}/{imageType}")
    @Produces({"image/jpeg"})
    public StreamingOutput groupImage(@PathParam("idGroup") final String idGroup, @PathParam("imageType") final String imageType) throws Exception {
        return new StreamingOutput() {
            public void write(OutputStream out) throws IOException {
                Configuration configuration = null;

                try {
                    configuration = GroupRestService.this.configurationService.loadByCode(ConfigurationEnum.PATH_BASE);
                    String base = configuration.getValue();
                    String path = base + "/group/" + idGroup + "/" + imageType + "_main.jpg";
                    ByteArrayInputStream in = new ByteArrayInputStream(FileUtil.readFile(path));
                    byte[] buf = new byte[16384];

                    for(int len = in.read(buf); len != -1; len = in.read(buf)) {
                        out.write(buf, 0, len);
                    }
                } catch (Exception var8) {
                    var8.printStackTrace();
                }

            }
        };
    }


    @PUT
    @Path("/saveImage")
    @Consumes({"application/json"})
    public void saveImage(PhotoUpload photoUpload) throws Exception {
        Group group = this.groupService.load(photoUpload.getIdObject());
        if (group == null) {
            throw new BusinessException("group_not_found");
        }

        int finalWidth = this.configurationService.loadByCode("SIZE_DETAIL_MOBILE").getValueAsInt();
        photoUpload.setFinalWidth(finalWidth);
        String idPhoto = photoUpload.getIdSubObject();
        String path = this.groupService.pathFilesGroup(group.getId());
        new PhotoUtils().saveImage(photoUpload, path, idPhoto);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/search")
    public List<Group> search(GroupSearch groupSearch)  throws Exception{
        return groupService.search(groupSearch);
    }


    @GET
    @Path("/listActiveUsers/{idGroup}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<UserGroup> listActiveUsers(@PathParam("idGroup") String idGroup) throws Exception {
        return groupInfoService.listActiveUsers(idGroup);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/listByUser")
    public List<Group> listByUser(GroupSearch groupSearch)  throws Exception{
        return groupService.listByUser(groupSearch);
    }
}
