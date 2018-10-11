package com.mangobits.startupkit.social.post;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangobits.startupkit.core.configuration.Configuration;
import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.photo.PhotoUpload;
import com.mangobits.startupkit.core.utils.FileUtil;
import com.mangobits.startupkit.notification.email.EmailService;
import com.mangobits.startupkit.service.admin.util.SecuredAdmin;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.like.Like;
import com.mangobits.startupkit.social.like.LikesService;
import com.mangobits.startupkit.social.post.Post;
import com.mangobits.startupkit.social.post.PostService;
import com.mangobits.startupkit.social.postInfo.PostInfo;
import com.mangobits.startupkit.social.postInfo.PostInfoService;
import com.mangobits.startupkit.social.userFavorites.UserFavorites;
import com.mangobits.startupkit.social.userFavorites.UserFavoritesService;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserService;
import com.mangobits.startupkit.user.util.SecuredUser;
import com.mangobits.startupkit.user.util.UserBaseRestService;
import com.mangobits.startupkit.ws.JsonContainer;
import javafx.geometry.Pos;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Path("/post")
public class PostRestService  extends UserBaseRestService {


    @EJB
    private PostService postService;

    @EJB
    private PostInfoService postInfoService;


    @EJB
    private UserFavoritesService userFavoritesService;

    @EJB
    private ConfigurationService configurationService;

    @EJB
    private EmailService emailService;

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/save")
    public String save(Post post)  throws Exception{

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            postService.save(post, true);
            cont.setData(post);

        } catch (Exception e) {
            handleException(cont, e, "saving a post");
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

            List<Post> list = postService.listAll();
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "listing all");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }


    @GET
    @Path("/listPending")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String listPending() throws Exception {

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            List<Post> list = postService.listPending();
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "listing pending");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/search")
    public String search(PostSearch postSearch)  throws Exception{

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            List<Post> list = postService.search(postSearch);
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "searching posts");
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

            Post post = postService.load(id);
            cont.setData(post);

        } catch (Exception e) {
            handleException(cont, e, "loading a post");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }


    @SecuredUser
    @GET
    @Path("/changeStatus/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String changeStatus(@PathParam("id") String id) throws Exception {

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            User user = getUserTokenSession();
            if (user == null){
                throw new BusinessException("user_not_found");
            }

            postService.changeStatus(id, user.getId());
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "changing post status");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @GET
    @Path("/favorite/{idPost}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String favorite(@PathParam("idPost") String idPost) throws Exception {

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            User user = getUserTokenSession();
            if (user == null){
                throw new BusinessException("user_not_found");
            }

            postService.favorite(idPost, user.getId());
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "making post favorite");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @GET
    @Path("/listFavorites/{idUser}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String listFavorites(@PathParam("idUser") String idUser) throws Exception {

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {
            List<Post> list = postService.listFavorites(idUser);
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "listing favorites");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredAdmin
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/changePostNewsStatus")
    public String changePostNewsStatus(Post post)  throws Exception{

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            postService.changePostNewsStatus(post);
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "changing post news status");
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }


    @GET
    @Path("/image/{idPost}/{imageType}")
    @Produces("image/jpeg")
    public StreamingOutput image(final @PathParam("idPost") String idPost, final @PathParam("imageType") String imageType) throws Exception {

        return out -> {

            try {

                Configuration configuration = configurationService.loadByCode(ConfigurationEnum.PATH_BASE);

//                String path = configuration.getValue() + "/post/" + idPost + "/" + imageType + "_main.jpg";
                String path = configuration.getValue() + "/post/" + idPost + "/main.jpg";


                File file = new File(path);
                if (!file.exists()) {
                    path = configuration.getValue() + "/post/" + "placeholder" + "/main.jpg";
                }
                ByteArrayInputStream in =  new ByteArrayInputStream(FileUtil.readFile(path));

                byte[] buf = new byte[16384];

                int len = in.read(buf);

                while(len!=-1) {

                    out.write(buf,0,len);

                    len = in.read(buf);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }



    @SecuredUser
    @POST
    @Path("/saveImage")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveImage(PhotoUpload photoUpload) throws Exception{

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            postService.saveImage(photoUpload);
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "saving post image");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/like")
    public String like(Like like)  throws Exception{

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            postService.like(like);
            cont.setDesc("OK");

        } catch (Exception e) {

            if(!(e instanceof BusinessException)){
                e.printStackTrace();
            }

            cont.setSuccess(false);
            cont.setDesc(e.getMessage());

            emailService.sendEmailError(e);
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/addComment")
    public String addComment(Comment comment)  throws Exception{

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            postService.addComment(comment);
            cont.setDesc("OK");

        } catch (Exception e) {

            if(!(e instanceof BusinessException)){
                e.printStackTrace();
            }

            cont.setSuccess(false);
            cont.setDesc(e.getMessage());

            emailService.sendEmailError(e);
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/removeComment")
    public String removeComment(Comment comment)  throws Exception{

        String resultStr = null;
        JsonContainer cont = new JsonContainer();

        try {

            User user = getUserTokenSession();
            if (user == null){
                throw new BusinessException("user_not_found");
            }

            postService.removeComment(comment, user.getId());
            cont.setDesc("OK");

        } catch (Exception e) {

            if(!(e instanceof BusinessException)){
                e.printStackTrace();
            }

            cont.setSuccess(false);
            cont.setDesc(e.getMessage());

            emailService.sendEmailError(e);
        }


        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }

    @SecuredUser
    @GET
    @Path("/listAllCommentsByPost/{idPost}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String listAllCommentsByPost(@PathParam("idPost") String idPost) throws Exception {

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {
            List<Comment> list = postInfoService.listActiveComments(idPost);
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "loading postInfo");
        }

        ObjectMapper mapper = new ObjectMapper();
        resultStr = mapper.writeValueAsString(cont);

        return resultStr;
    }
}