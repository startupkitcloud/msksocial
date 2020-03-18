package com.mangobits.startupkit.social.postInfo;

import com.mangobits.startupkit.social.comment.Comment;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PostInfoService {

    PostInfo retrieve(String idPost) throws Exception;

    List<Comment> listActiveComments(String idPost) throws Exception;

}
