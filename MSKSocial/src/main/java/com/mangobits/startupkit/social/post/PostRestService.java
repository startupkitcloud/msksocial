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
import com.mangobits.startupkit.social.postInfo.PostInfoService;
import com.mangobits.startupkit.social.spider.InfoUrl;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.util.SecuredUser;
import com.mangobits.startupkit.user.util.UserBaseRestService;
import com.mangobits.startupkit.ws.JsonContainer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.List;

@Stateless
@Path("/post")
public class PostRestService  extends UserBaseRestService {


    @EJB
    private PostService postService;

    @EJB
    private PostInfoService postInfoService;

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

    @GET
    @Path("/verifyUrl/{url}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public String verifyUrl(@PathParam("url") String url) throws Exception {

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            InfoUrl infoUrl = postService.verifyUrl(url);
            cont.setData(infoUrl);

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

            postService.changeStatus(id, user);
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

//    @SecuredUser
//    @GET
//    @Path("/listFavorites/{idUser}")
//    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//    public String listFavorites(@PathParam("idUser") String idUser) throws Exception {
//
//        String resultStr;
//        JsonContainer cont = new JsonContainer();
//
//        User user = getUserTokenSession();
//        if (user == null){
//            throw new BusinessException("user_not_found");
//        }
//        Double lat = null;
//        Double log = null;
//        if (user.getLastAddress()!= null && user.getLastAddress().getLatitude() != null){
//            lat = user.getLastAddress().getLatitude();
//            log = user.getLastAddress().getLongitude();
//        }
//
//        try {
//            List<Post> list = postService.listFavorites(idUser, lat, log);
//            cont.setData(list);
//
//        } catch (Exception e) {
//            handleException(cont, e, "listing favorites");
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        resultStr = mapper.writeValueAsString(cont);
//
//        return resultStr;
//    }

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/listFavorites")
    public String listFavorites(PostSearch postSearch)  throws Exception{

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            List<Post> list = postService.listFavorites(postSearch);
            cont.setData(list);

        } catch (Exception e) {
            handleException(cont, e, "searching favorites");
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

                String path = configuration.getValue() + "/post/" + idPost + "/" + imageType + "_main.jpg";
//                String path = configuration.getValue() + "/post/" + idPost + "/main.jpg";


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

    @POST
    @Path("/saveVideo")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public String saveVideo(PhotoUpload photoUpload) throws Exception{

        String resultStr;
        JsonContainer cont = new JsonContainer();

        try {

            postService.saveVideo(photoUpload);
            cont.setData("OK");

        } catch (Exception e) {
            handleException(cont, e, "saving post video");
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

    @GET
    @Path("/video/{idPost}/{videoType}")
    @Produces("video/mp4")
    public Response video(@HeaderParam("Range") String range, final @PathParam("idPost") String idPost, final @PathParam("videoType") String videoType) throws Exception {
        Configuration configuration = configurationService.loadByCode(ConfigurationEnum.PATH_BASE);
        String path = configuration.getValue() + "/post/" + idPost + "/" + videoType + "_original.mp4";
        return buildStream(new File(path), range);
    }


    private Response buildStream(final File asset, final String range) throws Exception {

        Response.ResponseBuilder res = null;

        try {

            // range not requested : Firefox, Opera, IE do not send range headers
            if (range == null) {
                StreamingOutput streamer = new StreamingOutput() {
                    @Override
                    public void write(final OutputStream output) throws IOException, WebApplicationException {

                        FileInputStream fos = new FileInputStream(asset);
                        final FileChannel inputChannel = fos.getChannel();
                        final WritableByteChannel outputChannel = Channels.newChannel(output);
                        try {
                            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                        }
                        catch (Exception e){

                        }finally {
                            // closing the channels
                            try {
                                inputChannel.close();
                                outputChannel.close();
                                fos.close();
                            } catch (Exception e2) {
                                // Jesus, no exceptions! =)
                            }
                        }
                    }
                };
                return Response.ok(streamer).header(HttpHeaders.CONTENT_LENGTH, asset.length()).build();
            }

            String[] ranges = range.split("=")[1].split("-");
            final int from = Integer.parseInt(ranges[0]);
            /**
             * Chunk media if the range upper bound is unspecified. Chrome sends "bytes=0-"
             */
            int to = 1000 + from;
            if (to >= asset.length()) {
                to = (int) (asset.length() - 1);
            }
            if (ranges.length == 2) {
                to = Integer.parseInt(ranges[1]);
            }

            final String responseRange = String.format("bytes %d-%d/%d", from, to, asset.length());
            final RandomAccessFile raf = new RandomAccessFile(asset, "r");
            raf.seek(from);

            final int len = to - from + 1;
            final MediaStreamer streamer = new MediaStreamer(len, raf);
            res = Response.status(Response.Status.PARTIAL_CONTENT).entity(streamer)
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Range", responseRange)
                    .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
                    .header(HttpHeaders.LAST_MODIFIED, new Date(asset.lastModified()));

        } catch (Exception e) {
            // TODO: handle exception
        }

        return res.build();
    }


    class MediaStreamer implements StreamingOutput {

        private int length;
        private RandomAccessFile raf;
        final byte[] buf = new byte[4096];

        public MediaStreamer(int length, RandomAccessFile raf) {
            this.length = length;
            this.raf = raf;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException, WebApplicationException {
            try {
                while( length != 0) {
                    int read = raf.read(buf, 0, buf.length > length ? length : buf.length);
                    outputStream.write(buf, 0, read);
                    length -= read;
                }
            } catch(Exception e){

            }finally {
                raf.close();
            }
        }

        public int getLenth() {
            return length;
        }
    }

}