package com.mangobits.startupkit.social.like;

import com.mangobits.startupkit.core.utils.AbstractDAO;


public class LikesDAO extends AbstractDAO<Likes> {

	public LikesDAO(){
		super(Likes.class);
	}
	

	@Override
	protected Object getId(Likes obj) {
		return obj.getIdOjbect();
	}
}
