package com.mangobits.startupkit.social.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangobits.startupkit.core.configuration.Configuration;
import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.photo.GalleryItem;
import com.mangobits.startupkit.core.photo.PhotoUpload;
import com.mangobits.startupkit.core.photo.PhotoUtils;
import com.mangobits.startupkit.core.utils.FileUtil;
import com.mangobits.startupkit.service.admin.util.AdminBaseRestService;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostSearch;
import com.mangobits.startupkit.social.post.PostService;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.util.SecuredUser;
import com.mangobits.startupkit.user.util.UserBaseRestService;
import com.mangobits.startupkit.ws.JsonContainer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
@Path("/group")
public class GroupRestService extends UserBaseRestService {

    @EJB
    private GroupService groupService;

    @EJB
    private ConfigurationService configurationService;

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/save")
    public String save(Group group)  throws Exception{

        User user = super.getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_not_found");
        }

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            Group groupBase = groupService.save(group, user);
            cont.setData(groupBase);

        } catch (Exception e) {
            handleException(cont, e, "saving a group");
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/addUser")
    public String addUser(UserGroup userGroup)  throws Exception{

        User user = super.getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_admin_not_found");
        }

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            groupService.addUser(userGroup, user);
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "adding a user");
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/removeUser")
    public String removeUser(UserGroup userGroup)  throws Exception{

        User user = super.getUserTokenSession();
        if (user == null){
            throw new BusinessException("user_admin_not_found");
        }


        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            groupService.removeUser(userGroup, user);
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "removing user");
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @GET
    @Path("/listAll")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String listAll() throws Exception {

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            List<Group> list = groupService.listAll();
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "listing all groups");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @GET
    @Path("/load/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String load(@PathParam("id") String id) throws Exception {

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            Group group = groupService.load(id);
            cont.setData(group);

        } catch (Exception e) {
            handleException(cont, e, "loading a group");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
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


    @POST
    @Path("/saveImage")
    @Produces({"application/json;charset=utf-8"})
    @Consumes({"application/json"})
    public String saveImage(PhotoUpload photoUpload) throws Exception {
        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {
            Group group = this.groupService.load(photoUpload.getIdObject());
            if (group == null) {
                throw new BusinessException("group_not_found");
            }

            int finalWidth = this.configurationService.loadByCode("SIZE_DETAIL_MOBILE").getValueAsInt();
            photoUpload.setFinalWidth(finalWidth);
            String idPhoto = photoUpload.getIdSubObject();
            String path = this.groupService.pathFilesGroup(group.getId());
             new PhotoUtils().saveImage(photoUpload, path, idPhoto);
//            this.groupService.save(group);
            cont.setDesc("OK");
        } catch (Exception var9) {
            if (!(var9 instanceof BusinessException)) {
                var9.printStackTrace();
            }

            cont.setSuccess(false);
            cont.setDesc(var9.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);
        return resultStr;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/search")
    public String search(GroupSearch groupSearch)  throws Exception{

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            List<Group> list = groupService.search(groupSearch);
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "searching groups");
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }


}
