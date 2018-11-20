package com.mangobits.startupkit.social.post;


import com.mangobits.startupkit.core.configuration.Configuration;
import com.mangobits.startupkit.core.configuration.ConfigurationEnum;
import com.mangobits.startupkit.core.configuration.ConfigurationService;
import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.dao.SearchProjection;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.core.photo.GalleryItem;
import com.mangobits.startupkit.core.photo.PhotoUpload;
import com.mangobits.startupkit.core.photo.PhotoUtils;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import com.mangobits.startupkit.core.utils.BusinessUtils;
import com.mangobits.startupkit.notification.NotificationBuilder;
import com.mangobits.startupkit.notification.NotificationService;
import com.mangobits.startupkit.notification.TypeSendingNotificationEnum;
import com.mangobits.startupkit.social.comment.Comment;
import com.mangobits.startupkit.social.group.Group;
import com.mangobits.startupkit.social.group.GroupService;
import com.mangobits.startupkit.social.group.UserGroup;
import com.mangobits.startupkit.social.like.Like;
import com.mangobits.startupkit.social.like.LikesService;
import com.mangobits.startupkit.social.postInfo.PostInfo;
import com.mangobits.startupkit.social.postInfo.PostInfoDAO;
import com.mangobits.startupkit.social.spider.InfoUrl;
import com.mangobits.startupkit.social.userFavorites.UserFavorites;
import com.mangobits.startupkit.social.userFavorites.UserFavoritesService;
import com.mangobits.startupkit.user.User;
import com.mangobits.startupkit.user.UserCard;
import com.mangobits.startupkit.user.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.spatial.DistanceSortField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javax.ejb.*;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostServiceimpl implements PostService {


    private static final int TOTAL_POSTS_PAGE = 10;


    @New
    @Inject
    private PostDAO postDAO;

    @New
    @Inject
    private PostInfoDAO postInfoDAO;


    @EJB
    private ConfigurationService configurationService;

    @EJB
    private LikesService likesService;

    @EJB
    private GroupService groupService;

    @EJB
    private UserService userService;

    @EJB
    private UserFavoritesService userFavoritesService;

    @EJB
    private NotificationService notificationService;



    @Override
    public void changeStatus(String idPost, User user) throws Exception {

        Post post = retrieve(idPost);

        if (post == null){
            throw new BusinessException("post_not_found");
        }


        if (!post.getUserCreator().getId().equals(user.getId())){
            if (user.getType() == null || !user.getType().equals("ADMIN")){
                throw new BusinessException("user_must_be_creator_or_admin");
            }
        }

        if(post.getStatus().equals(PostStatusEnum.ACTIVE)){
            post.setStatus(PostStatusEnum.BLOCKED);
        }
        else{
            post.setStatus(PostStatusEnum.ACTIVE);
        }

        save(post, false);

    }

    @Override
    public void changePostNewsStatus(Post post) throws Exception {

        if(post.getId() == null){
            throw new BusinessException("missing_post_id");
        }

        if(post.getType() == null){
            throw new BusinessException("missing_post_type");
        }

        if(post.getType() != PostTypeEnum.NEWS){
            throw new BusinessException("post_type_must_be_news");
        }

        if(post.getStatus() == null){
            throw new BusinessException("missing_post_status");
        }

        Post postBase = postDAO.retrieve(new Post(post.getId()));

        if(postBase == null){
            throw new BusinessException("post_not_found");
        }

        postBase.setStatus(post.getStatus());
        postDAO.update(postBase);

        if(postBase.getType().equals(PostTypeEnum.NEWS)){
            Configuration configuration =configurationService.loadByCode("NOTIFY_USERS");

            if(configuration != null){
                sendMessageChangePostNewsStatus();
            }

        }

    }

    private void sendMessageChangePostNewsStatus() throws Exception {

        List<UserCard> listUserCard = this.userService.listAll();

        if(CollectionUtils.isNotEmpty(listUserCard)){

            for(UserCard userCard : listUserCard){
               // sendNotification();
            }
        }
    }

    @Override
    public void save(Post post, Boolean sendGroupMessage) throws Exception {

        if(post.getStatus() == null){
            post.setStatus(PostStatusEnum.ACTIVE);
        }

        if(post.getId() == null){
            post.setCreationDate(new Date());
            postDAO.insert(post);

            if (post.getIdGroup() != null && sendGroupMessage){
                sendGroupMessage(post.getIdGroup(), post.getId());
            }
        }else {
            new BusinessUtils<>(postDAO).basicSave(post);
        }

    }


    @Asynchronous
    private void sendGroupMessage(String idGroup, String idPost) throws Exception{

        Group group = groupService.load(idGroup);

        if (group == null){
            throw new BusinessException("group_not_found");

        }
        if (group.getListUsers() != null){

            for (UserGroup userGroup : group.getListUsers()){
                User user = userService.retrieve(userGroup.getIdUser());
                sendNotification(user, group.getTitle(), idPost, "Publicou um post", "GROUP_POST");

            }
        }

    }

    @Override
    public void addComment(Comment comment) throws Exception {


        if(comment.getIdPost() == null){
            throw new BusinessException("missing_idPost");
        }
        if(comment.getStatus() == null){
            comment.setStatus(SimpleStatusEnum.ACTIVE);
        }

        if(comment.getId() == null){
            comment.setCreationDate(new Date());
            comment.setId(UUID.randomUUID().toString());
        }

        // adiciona o comentário no postInfo
        PostInfo postInfo = postInfoDAO.retrieve(new PostInfo(comment.getIdPost()));
        if (postInfo == null){
            postInfo = new PostInfo();
            postInfo.setId(comment.getIdPost());
            postInfo.setListActiveComments(new ArrayList<>());
            postInfo.getListActiveComments().add(comment);
            postInfoDAO.insert(postInfo);
        }else {
            if (postInfo.getListActiveComments() == null){
                postInfo.setListActiveComments(new ArrayList<>());
            }
            postInfo.getListActiveComments().add(comment);
            postInfoDAO.update(postInfo);
        }

        // atualiza o comments do post
        Post postBase = retrieve(comment.getIdPost());

        if (postBase == null){
            throw new BusinessException("post_not_found");
        }

        if (postBase.getComments() == null){
            postBase.setComments(0);
        }
        postBase.setComments(postBase.getComments() + 1);

        // pega os 3 últimos posts e adiciona no postBase
        List<Comment> allComments = postInfo.getListActiveComments();
        int subListCount = allComments.size();
        if (subListCount > 3){
           subListCount = 3;
        }
        List<Comment> lastComments = postInfo.getListActiveComments().subList(0, subListCount);
        postBase.setLastComments(lastComments);

        postDAO.update(postBase);

    }

    @Override
    public void removeComment(Comment comment, String idUser) throws Exception {

        if (!comment.getIdUser().equals(idUser)){
            throw new BusinessException("user_must_be_creator");
        }

        if(comment.getIdPost() == null){
            throw new BusinessException("missing_idPost");
        }
        if(comment.getId() == null){
            throw new BusinessException("missing_idComment");
        }

        PostInfo postInfo = postInfoDAO.retrieve(new PostInfo(comment.getIdPost()));
        if (postInfo == null){
            throw new BusinessException("postInfo_not_found");
        }
        if (postInfo.getListActiveComments() == null || postInfo.getListActiveComments().size() == 0){
            throw new BusinessException("activeCommentsList_not_found");
        }

        Comment commentbase = postInfo.getListActiveComments().stream()
                .filter(p -> p.getId().equals(comment.getId()))
                .findFirst()
                .orElse(null);

        if (commentbase == null) {
            throw new BusinessException("comment_not_found");
        }
        postInfo.getListActiveComments().remove(commentbase);

        if (postInfo.getListBlockedComments() == null){
           postInfo.setListBlockedComments(new ArrayList<>());
        }
        commentbase.setStatus(SimpleStatusEnum.BLOCKED);
        postInfo.getListBlockedComments().add(commentbase);
        postInfoDAO.update(postInfo);

        // atualiza o comments do post
        Post postBase = retrieve(comment.getIdPost());

        if (postBase == null){
            throw new BusinessException("post_not_found");
        }

        if (postBase.getComments() > 0){
            postBase.setComments(postBase.getComments() - 1);
        }

        // pega os 3 últimos posts e adiciona no postBase
        List<Comment> lastComments = postInfo.getListActiveComments().subList(0, 2);
        postBase.setLastComments(lastComments);

        postDAO.update(postBase);

    }

    @Override
    public List<Post> listAll() throws Exception {

        List<Post> list = this.postDAO.search((new SearchBuilder()).appendParam("status", PostStatusEnum.ACTIVE).build());

        return list;
    }


    @Override
    public List<Post> listPending() throws Exception {

        List<Post> list = this.postDAO.search((new SearchBuilder()).appendParam("status", PostStatusEnum.PENDING).build());

        return list;
    }

    @Override
    public List<Post> search(PostSearch postSearch) throws Exception {

        SearchBuilder searchBuilder = new SearchBuilder();
        searchBuilder.appendParam("status", PostStatusEnum.ACTIVE);
        if (postSearch.getQueryString() != null && StringUtils.isNotEmpty(postSearch.getQueryString().trim())) {
            searchBuilder.appendParam("title|desc", postSearch.getQueryString());
        }
        if (postSearch.getIdGroup() != null) {
            searchBuilder.appendParam("idGroup", postSearch.getIdGroup());
        }

        if (postSearch.getIdUserCreator() != null) {
            searchBuilder.appendParam("userCreator.id", postSearch.getIdUserCreator());
        }

        searchBuilder.setFirst(TOTAL_POSTS_PAGE * (postSearch.getPage() -1));
        searchBuilder.setMaxResults(TOTAL_POSTS_PAGE);
        Sort sort = new Sort(new SortField("creationDate", SortField.Type.LONG, true));
        searchBuilder.setSort(sort);
        if (postSearch.getLat() != null && postSearch.getLog() != null){
            searchBuilder.setProjection(new SearchProjection(postSearch.getLat(), postSearch.getLog(), "address", "distance"));
        }

        //ordena

        List<Post> list = this.postDAO.search(searchBuilder.build());

        //atualiza todos com com view + 1
        if(list != null){
            for(Post post : list){

                if(post.getTotalViews() == null){
                    post.setTotalViews(0);
                }

                post.setTotalViews(post.getTotalViews()+1);
                postDAO.update(post);

                Date creationDate = post.getCreationDate();
                Date now = new Date();

                long diff = now.getTime() - creationDate.getTime();
//                long diffMinutes = diff / (60 * 1000) % 60;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                Integer i = (int) (long) minutes;
                post.setTime(i);

                //chamar o dao que faz o aggregate e retorna uma lista
                List<Comment> comments = postInfoDAO.listActiveComments(post.getId(), 3);
                post.setLastComments(comments);

                if (postSearch.getIdUser() != null) {

                    // verifica se o post foi curtido
                    List<String> listIdPostLiked = listIdPostLiked(postSearch.getIdUser());

                    String postLiked = listIdPostLiked.stream()
                            .filter(p -> p.equals(post.getId()))
                            .findFirst()
                            .orElse(null);

                    if (postLiked != null) {
                        post.setFgLiked(true);
                    }else {
                        post.setFgLiked(false);
                    }

                    // verifica se o post foi favoritado
                    List<Post> listPostFavorite = listPostFavorite(postSearch.getIdUser());
                    Post postFavorited = listPostFavorite.stream()
                            .filter(p -> p.getId().equals(post.getId()))
                            .findFirst()
                            .orElse(null);

                    if (postFavorited != null) {
                        post.setFgFavorite(true);
                    }else {
                        post.setFgFavorite(false);
                    }

                }
            }
        }

        return list;
    }


    @Override
    public Post retrieve(String idPost) throws Exception {

        Post post =  postDAO.retrieve(new Post(idPost));

        return post;
    }

    @Override
    public void saveImage(PhotoUpload photoUpload) throws Exception{

        Post post = retrieve(photoUpload.getIdObject());

        if(post == null){
            throw new BusinessException("post_not_found");
        }

        //get the final size
        int finalWidth = configurationService.loadByCode("SIZE_DETAIL_MOBILE").getValueAsInt();
        photoUpload.setFinalWidth(finalWidth);

        GalleryItem gi = new GalleryItem();
        gi.setId(photoUpload.getIdSubObject());

        if(post.getGallery() == null){
            post.setGallery(new ArrayList<>());
        }

        GalleryItem item = post.getGallery().stream()
                .filter(p -> p.getId().equals(gi.getId()))
                .findFirst()
                .orElse(null);


        if(item == null){
            post.getGallery().add(gi);
            postDAO.update(post);
        }

        String path = configurationService.loadByCode(ConfigurationEnum.PATH_BASE).getValue() + "/post/" + post.getId();

        new PhotoUtils().saveImage(photoUpload, path, gi.getId());
    }

    @Override
    public void saveVideo(PhotoUpload photoUpload) throws Exception{

        Post post = retrieve(photoUpload.getIdObject());

        if(post == null){
            throw new BusinessException("post_not_found");
        }

        //get the final size
        int finalWidth = configurationService.loadByCode("SIZE_DETAIL_MOBILE").getValueAsInt();
        photoUpload.setFinalWidth(finalWidth);

        GalleryItem gi = new GalleryItem();
        gi.setId(photoUpload.getIdSubObject());

        if(post.getGallery() == null){
            post.setGallery(new ArrayList<>());
        }

        GalleryItem item = post.getGallery().stream()
                .filter(p -> p.getId().equals(gi.getId()))
                .findFirst()
                .orElse(null);


        if(item == null){
            post.getGallery().add(gi);
            postDAO.update(post);
        }

        String path = configurationService.loadByCode(ConfigurationEnum.PATH_BASE).getValue() + "/post/" + post.getId();

        new PhotoUtils().saveVideo(photoUpload, path, gi.getId());
    }

    @Override
    public List<Post> searchByNewsUrl(String newsUrl) throws Exception {

        return postDAO.search(new SearchBuilder()
                .appendParam("infoUrl.url", newsUrl)
                .build());
    }

    @Override
    public Post load(String id) throws Exception {
        return postDAO.retrieve(new Post(id));
    }



    @Override
    public void like(Like like) throws Exception {

        if (like.getIdObjectLiked() == null){
            throw new BusinessException("missing_object_liked");
        }
        if (like.getIdObjectLiker() == null){
            throw new BusinessException("missing_object_liker");
        }

        if (like.getTypeObjectLiked().equals("POST")){

            Boolean remove = likesService.like(like);

            Post post = postDAO.retrieve(new Post(like.getIdObjectLiked()));

            if (post == null){
                throw new BusinessException("post_not_found");
            }

            if (remove){

                if (post.getLikes() != null && post.getLikes() > 0){
                    post.setLikes(post.getLikes() - 1);
                }else {
                    post.setLikes(0);
                }

            }else {

                if (post.getLikes() != null){
                    post.setLikes(post.getLikes() + 1);
                }else {
                    post.setLikes(1);
                }

            }

            new BusinessUtils<>(postDAO).basicSave(post);

        }

    }

    private List<String> listIdPostLiked(String idUser) throws Exception {

        List<Like> listLikes = (List<Like>) likesService.listILike(idUser, "POST");
        List<String> listIdPosts = new ArrayList<>();

        for(Like like : listLikes){

            String idPost = like.getIdObjectLiked();
            listIdPosts.add(idPost);
        }

        return listIdPosts;

    }

    @Override
    public Boolean favorite (String idPost, String idUser) throws Exception {

       Boolean remove = userFavoritesService.favoritePost(idPost, idUser);

       return remove;

    }


    private List<Post> listPostFavorite(String idUser) throws Exception {

        UserFavorites userFavorites =  userFavoritesService.load(idUser);
        List<Post> list = new ArrayList<>();

        if (userFavorites != null && userFavorites.getListFavorites() != null) {
            list = userFavorites.getListFavorites();
        }

        return list;

    }

    @Override
    public List<Post> listFavorites(String idUser) throws Exception {

        List<Post> list = listPostFavorite(idUser);

        // verifica se o post foi curtido
        List<String> listIdPostLiked = listIdPostLiked(idUser);

        for (Post post : list){

            String postLiked = listIdPostLiked.stream()
                    .filter(p -> p.equals(post.getId()))
                    .findFirst()
                    .orElse(null);

            if (postLiked != null) {
                post.setFgLiked(true);
            }else {
                post.setFgLiked(false);
            }

            post.setFgFavorite(true);
        }

        return list;

    }

    private void sendNotification(User user, String title, String link, String msg, String type) throws Exception {
        notificationService.sendNotification(new NotificationBuilder()
                .setTo(user)
                .setTypeSending(TypeSendingNotificationEnum.APP)
                .setTypeFrom(type)
                .setTitle(title)
                .setIdLink(link)
                .setMessage(msg)
                .build());
    }

    @Override
    public InfoUrl verifyUrl(String url) throws Exception {

        InfoUrl infoUrl = new InfoUrl();

        try {

            Document doc = Jsoup.connect(url).get();
            Element elTitle = doc.select("meta[name=title]").first();

            if(elTitle != null){
                infoUrl.setTitle(elTitle.attr("content"));
            }
            else{
                infoUrl.setTitle(doc.select("title").first().html());
            }


            Element elFoto = doc.select("meta[property=og:image]").first();
            if(elFoto != null){
                infoUrl.setUrlPhoto(elFoto.attr("content"));
            }

            infoUrl.setUrl(url);


        } catch (Exception e) {
            infoUrl = null;
        }

        return infoUrl;
    }

    @Override
    public String videoPath() throws Exception {

        String path = null;

        path = configurationService.loadByCode("PATH_BASE").getValue() + "/videos/";


        return path;
    }




}