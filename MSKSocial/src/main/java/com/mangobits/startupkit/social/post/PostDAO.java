package com.mangobits.startupkit.social.post;

import com.mangobits.startupkit.core.dao.AbstractDAO;

public class PostDAO extends AbstractDAO<Post> {

    public PostDAO(){
        super(Post.class);
    }


    @Override
    public Object getId(Post obj) {
        return obj.getId();
    }
}
