package org.startupkit.social.like;

import org.startupkit.core.dao.AbstractDAO;


public class LikesDAO extends AbstractDAO<Likes> {

	public LikesDAO(){
		super(Likes.class);
	}

	@Override
	public Object getId(Likes obj) {
		return obj.getIdOjbect();
	}
}
