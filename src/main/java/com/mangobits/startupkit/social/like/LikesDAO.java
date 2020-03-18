package com.mangobits.startupkit.social.like;

import com.mangobits.startupkit.core.dao.AbstractDAO;


public class LikesDAO extends AbstractDAO<Likes> {

	public LikesDAO(){
		super(Likes.class);
	}
	

	@Override
	public Object getId(Likes obj) {
		return obj.getIdOjbect();
	}
}
