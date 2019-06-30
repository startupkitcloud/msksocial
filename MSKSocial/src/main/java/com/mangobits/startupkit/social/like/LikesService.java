package com.mangobits.startupkit.social.like;

import javax.ejb.Local;
import java.util.List;


@Local
public interface LikesService {


	Boolean like(Like like) throws Exception;


	List<? extends Like> listLikesMe(String idObject, String typeObject) throws Exception;


	List<? extends Like> listILike(String idObject, String typeObject) throws Exception;
}
