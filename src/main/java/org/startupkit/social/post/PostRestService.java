package org.startupkit.social.post;


import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.startupkit.admin.userb.UserBService;
import org.startupkit.admin.util.SecuredAdmin;
import org.startupkit.core.configuration.Configuration;
import org.startupkit.core.configuration.ConfigurationEnum;
import org.startupkit.core.configuration.ConfigurationService;
import org.startupkit.core.exception.BusinessException;
import org.startupkit.core.photo.PhotoUpload;
import org.startupkit.core.photo.PhotoUtils;
import org.startupkit.core.utils.FileUtil;
import org.startupkit.notification.email.EmailService;
import org.startupkit.social.comment.Comment;
import org.startupkit.social.like.Like;
import org.startupkit.social.postInfo.PostInfoService;
import org.startupkit.social.spider.InfoUrl;
import org.startupkit.social.spider.SpiderService;
import org.startupkit.user.User;
import org.startupkit.user.UserService;
import org.startupkit.user.util.SecuredUser;
import org.startupkit.user.util.UserBaseRestService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
@Path("/post")
public class PostRestService extends UserBaseRestService {


    @EJB
    private PostService postService;

    @EJB
    private PostInfoService postInfoService;

    @EJB
    private UserBService userBService;

    @EJB
    private UserService userService;

    @Context
    private HttpServletRequest requestB;

    @EJB
    private ConfigurationService configurationService;

    @EJB
    private EmailService emailService;

    @EJB
    private SpiderService spiderService;

    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/save")
    public Post save(Post post) throws Exception {

        String authorizationHeader = this.requestB.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer".length()).trim();
            post.setIdObj(this.userService.retrieveByToken(token).getCode());
        }

        postService.save(post, true);
        return post;
    }


    @SecuredAdmin
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/saveAdmin")
    public Post saveAdmin(Post post) throws Exception {

        String authorizationHeader = this.requestB.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer".length()).trim();
            post.setIdObj(this.userBService.retrieveByToken(token).getIdObj());
        }
        postService.save(post, true);
        return post;
    }


    @GET
    @Path("/listAll")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Post> listAll() throws Exception {
        return postService.listAll();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/listPending")
    public PostResultSearch listPending(PostSearch postSearch) throws Exception {
        return postService.listPending(postSearch);
    }


    @SecuredAdmin
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/searchAdmin")
    public PostResultSearch searchAdmin(PostSearch postSearch) throws Exception {

        String authorizationHeader = this.requestB.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer".length()).trim();
            postSearch.setIdObj(this.userBService.retrieveByToken(token).getIdObj());
        }
        return postService.searchAdmin(postSearch);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/search")
    public List<Post> search(PostSearch postSearch) throws Exception {
        return postService.search(postSearch);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/simpleSearch")
    public List<Post> simpleSearch(PostSearch postSearch) throws Exception {
        return postService.simpleSearch(postSearch);
    }


    @GET
    @Path("/load/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Post load(@PathParam("id") String id) throws Exception {
        return postService.load(id);
    }


    @GET
    @Path("/verifyUrl/{url}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public InfoUrl verifyUrl(@PathParam("url") String url) throws Exception {
        return postService.verifyUrl(url);
    }


    @SecuredUser
    @PUT
    @Path("/changeStatus/{id}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void changeStatus(@PathParam("id") String id) throws Exception {

        User user = getUserTokenSession();
        if (user == null) {
            throw new BusinessException("user_not_found");
        }

        postService.changeStatus(id, user);
    }


    @SecuredUser
    @PUT
    @Path("/favorite/{idPost}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public void favorite(@PathParam("idPost") String idPost) throws Exception {

        User user = getUserTokenSession();
        if (user == null) {
            throw new BusinessException("user_not_found");
        }

        postService.favorite(idPost, user.getId());
    }


    @SecuredUser
    @GET
    @Path("/listFavorites/{idUser}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Post>  listFavorites(@PathParam("idUser") String idUser) throws Exception {

        User user = getUserTokenSession();
        if (user == null) {
            throw new BusinessException("user_not_found");
        }
        Double lat = null;
        Double log = null;
        if (user.getLastAddress() != null && user.getLastAddress().getLatitude() != null) {
            lat = user.getLastAddress().getLatitude();
            log = user.getLastAddress().getLongitude();
        }

        PostSearch postSearch = new PostSearch();
        postSearch.setIdUser(idUser);
        postSearch.setLat(lat);
        postSearch.setLog(log);
        return postService.listFavorites(postSearch);
    }


    @SecuredUser
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("/listFavorites")
    public List<Post> listFavorites(PostSearch postSearch) throws Exception {
        return postService.listFavorites(postSearch);
    }


    @SecuredAdmin
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/changePostNewsStatus")
    public void changePostNewsStatus(Post post) throws Exception {
        postService.changePostNewsStatus(post);
    }


    @GET
    @Path("/image/{idPost}/{imageType}")
    @Produces("image/jpeg")
    public StreamingOutput image(final @PathParam("idPost") String idPost, final @PathParam("imageType") String imageType) throws Exception {

        return out -> {

            try {

                Configuration configuration = configurationService.loadByCode(ConfigurationEnum.PATH_BASE);
                String path = configuration.getValue() + "/post/" + idPost + "/" + imageType + "_main.jpg";

                File file = new File(path);
                if (!file.exists()) {
                    path = configuration.getValue() + "/post/" + "placeholder" + "/main.jpg";
                }
                ByteArrayInputStream in = new ByteArrayInputStream(FileUtil.readFile(path));

                byte[] buf = new byte[16384];

                int len = in.read(buf);

                while (len != -1) {

                    out.write(buf, 0, len);

                    len = in.read(buf);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }


    @SecuredUser
    @PUT
    @Path("/saveImage")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveImage(PhotoUpload photoUpload) throws Exception {
        postService.saveImage(photoUpload);
    }


    @PUT
    @Path("/saveVideo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveVideo(PhotoUpload photoUpload) throws Exception {
        postService.saveVideo(photoUpload);
    }


    @PUT
    @Path("/saveVideoByParts")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveVideoByParts(PhotoUpload photoUpload) throws Exception {
        postService.saveVideoByParts(photoUpload);
    }


    @SecuredUser
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/like")
    public void like(Like like) throws Exception {
        postService.like(like);
    }


    @SecuredUser
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addComment")
    public void addComment(Comment comment) throws Exception {
        postService.addComment(comment);
    }


    @SecuredUser
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/removeComment")
    public void removeComment(Comment comment) throws Exception {
        User user = getUserTokenSession();
        if (user == null) {
            throw new BusinessException("user_not_found");
        }

        postService.removeComment(comment, user.getId());
    }


    @SecuredUser
    @GET
    @Path("/listAllCommentsByPost/{idPost}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public List<Comment> listAllCommentsByPost(@PathParam("idPost") String idPost) throws Exception {
        return postInfoService.listActiveComments(idPost);
    }


    @PUT
    @Path("/goSpider")
    public void goSpider() throws Exception {
        spiderService.goSpider();
    }


    @GET
    @Path("/video/{idPost}")
    @Produces("video/mp4")
    public Response video(@HeaderParam("Range") String range, final @PathParam("idPost") String idPost) throws Exception {
        return buildStream(new File(postService.videoPath(idPost)), range);
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
                        } catch (Exception e) {

                        } finally {
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
            res = Response.status(Status.PARTIAL_CONTENT).entity(streamer)
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
                while (length != 0) {
                    int read = raf.read(buf, 0, buf.length > length ? length : buf.length);
                    outputStream.write(buf, 0, read);
                    length -= read;
                }
            } catch (Exception e) {

            } finally {
                raf.close();
            }
        }

        public int getLenth() {
            return length;
        }
    }


    @POST
    @Path("/uploadVideo")
    @Consumes("multipart/form-data")
    public String uploadVideo(MultipartFormDataInput input) throws IOException {

        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            InputPart inputPartsId = uploadForm.get("idObj").get(0);
            String photoId = inputPartsId.getBody(String.class, null);

            InputPart inputPartsName = uploadForm.get("name").get(0);
            String photoName = inputPartsName.getBody(String.class, null);

            InputPart videoFile = uploadForm.get("video_file").get(0);

            PhotoUpload photoUpload = new PhotoUpload();
            InputStream inputStream = videoFile.getBody(InputStream.class, null);

            byte[] bytes = IOUtils.toByteArray(inputStream);
            photoUpload.setPhotoBytes(bytes);
            photoUpload.setIdObject(photoId);
            photoUpload.setIdSubObject("video");
            photoUpload.setTitle(photoName);

            postService.saveVideo(photoUpload);

            InputPart imageFile = uploadForm.get("image_file").get(0);

            photoUpload = new PhotoUpload();
            inputStream = imageFile.getBody(InputStream.class, null);
            bytes = IOUtils.toByteArray(inputStream);
            photoUpload.setIdObject(photoId);
            photoUpload.setPhotoBytes(bytes);
            photoUpload.setIdSubObject("video");
            photoUpload.setTitle(photoName);

            Configuration configuration = configurationService.loadByCode(ConfigurationEnum.PATH_BASE);
            String path = configuration.getValue() + "/post/" + photoId;

            // salva imagem do video
            new PhotoUtils().saveImage(photoUpload, path, photoUpload.getIdSubObject());


            StringBuilder textReturn = new StringBuilder();
            textReturn.append("{");
            textReturn.append("\n");
            textReturn.append("\"idVideo\":" + "\"");
            textReturn.append("video");
            textReturn.append("\",");
            textReturn.append("\n");
            textReturn.append("\"name\":" + "\"");
            textReturn.append(photoName);
            textReturn.append("\"");
            textReturn.append("\n");
            textReturn.append("}");


            return textReturn.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    @POST
    @Path("/uploadVideoAsync")
    @Consumes("multipart/form-data")
    public String uploadVideoAsync(MultipartFormDataInput input) throws IOException {

        try {
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            InputPart inputPartsId = uploadForm.get("idObj").get(0);
            String photoId = inputPartsId.getBody(String.class, null);

            InputPart inputPartsName = uploadForm.get("name").get(0);
            String photoName = inputPartsName.getBody(String.class, null);

            InputPart videoFile = uploadForm.get("video_file").get(0);

            PhotoUpload photoUpload = new PhotoUpload();
            InputStream inputStream = videoFile.getBody(InputStream.class, null);

            byte[] bytes = IOUtils.toByteArray(inputStream);
            photoUpload.setPhotoBytes(bytes);
            photoUpload.setIdObject(photoId);
            photoUpload.setIdSubObject("video");
            photoUpload.setTitle(photoName);

            postService.saveVideoAsync(photoUpload);

            InputPart imageFile = uploadForm.get("image_file").get(0);

            photoUpload = new PhotoUpload();
            inputStream = imageFile.getBody(InputStream.class, null);
            bytes = IOUtils.toByteArray(inputStream);
            photoUpload.setIdObject(photoId);
            photoUpload.setPhotoBytes(bytes);
            photoUpload.setIdSubObject("video");
            photoUpload.setTitle(photoName);

            Configuration configuration = configurationService.loadByCode(ConfigurationEnum.PATH_BASE);
            String path = configuration.getValue() + "/post/" + photoId;

            // salva imagem do video
            new PhotoUtils().saveImage(photoUpload, path, photoUpload.getIdSubObject());


            StringBuilder textReturn = new StringBuilder();
            textReturn.append("{");
            textReturn.append("\n");
            textReturn.append("\"idVideo\":" + "\"");
            textReturn.append("video");
            textReturn.append("\",");
            textReturn.append("\n");
            textReturn.append("\"name\":" + "\"");
            textReturn.append(photoName);
            textReturn.append("\"");
            textReturn.append("\n");
            textReturn.append("}");


            return textReturn.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    @GET
    @Path("/checkUploadVideo/{idPost}/{idVideo}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Boolean checkUploadVideo(@PathParam("idPost") String idPost,
                                    @PathParam("idVideo") String idVideo) throws Exception {

        Configuration configuration = configurationService.loadByCode(ConfigurationEnum.PATH_BASE);

        String path = configuration.getValue() + "/post/" + idPost + "/" + idVideo + "_post.txt";

        boolean fgVideo = false;

        File file = new File(path);

        if (file.exists()) {
            fgVideo = true;
        }

        return fgVideo;
    }
}
