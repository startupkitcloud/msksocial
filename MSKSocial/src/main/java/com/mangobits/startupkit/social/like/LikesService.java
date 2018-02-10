package com.mangobits.startupkit.social.like;

import com.mangobits.startupkit.core.exception.ApplicationException;
import com.mangobits.startupkit.core.exception.BusinessException;

import javax.ejb.Local;
import java.util.List;


@Local
public interface LikesService {


	void like(Like like) throws ApplicationException, BusinessException;


	List<Like> listLikesMe(String idObject) throws BusinessException, ApplicationException;


	List<Like> listILike(String idObject) throws BusinessException, ApplicationException;
}
