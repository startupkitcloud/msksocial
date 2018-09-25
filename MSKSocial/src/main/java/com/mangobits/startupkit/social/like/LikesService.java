package com.mangobits.startupkit.social.like;

import com.mangobits.startupkit.core.exception.ApplicationException;
import com.mangobits.startupkit.core.exception.BusinessException;

import javax.ejb.Local;
import java.util.List;


@Local
public interface LikesService {


	Boolean like(Like like) throws Exception;


	List<? extends Like> listLikesMe(String idObject) throws Exception;


	List<? extends Like> listILike(String idObject) throws Exception;
}
