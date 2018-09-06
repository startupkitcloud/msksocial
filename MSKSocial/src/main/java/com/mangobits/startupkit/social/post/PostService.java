package com.mangobits.startupkit.social.post;

import com.mangobits.startupkit.core.photo.PhotoUpload;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PostService {

    void changeStatus(String idPost) throws Exception;

    void save(Post post) throws Exception;

    List<Post> listAll() throws Exception;

    Post retrieve(String idPost) throws Exception;

    void saveImage(PhotoUpload photoUpload) throws Exception;

    List<Post> searchByNewsUrl(String newsUrl) throws Exception;
}
