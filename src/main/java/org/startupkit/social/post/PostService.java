package org.startupkit.social.post;

import org.startupkit.core.photo.PhotoUpload;
import org.startupkit.social.comment.Comment;
import org.startupkit.social.like.Like;
import org.startupkit.social.spider.InfoUrl;
import org.startupkit.user.User;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PostService {

    void changeStatus(String idPost, User user) throws Exception;

    void changePostNewsStatus(Post post) throws Exception;


    void save(Post post, Boolean sendGroupMessage) throws Exception;

    List<Post> listAll() throws Exception;

    PostResultSearch listPending(PostSearch postSearch) throws Exception;

    List<Post> search(PostSearch postSearch) throws Exception;

    List<Post> simpleSearch(PostSearch postSearch) throws Exception;


    Post retrieve(String idPost) throws Exception;

    void saveImage(PhotoUpload photoUpload) throws Exception;

    List<Post> searchByNewsUrl(String newsUrl) throws Exception;

    Post load(String id) throws Exception;

    void like(Like like) throws Exception;

    void removeComment(Comment comment, String idUser) throws Exception;

    void addComment(Comment comment) throws Exception;

    Boolean favorite (String idPost, String idUser) throws Exception;

    List<Post> listFavorites(PostSearch postSearch) throws Exception;

    InfoUrl verifyUrl(String url) throws Exception;

    String videoPath(String idPost) throws Exception;

    void saveVideo(PhotoUpload photoUpload) throws Exception;

    void saveVideoAsync(PhotoUpload photoUpload) throws Exception;

    void saveVideoByParts(PhotoUpload photoUpload) throws Exception;

    void blockExpiredPendingPosts() throws Exception;

    PostResultSearch searchAdmin(PostSearch postSearch) throws Exception;
}
