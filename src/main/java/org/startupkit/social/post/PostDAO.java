package org.startupkit.social.post;

import org.startupkit.core.dao.AbstractDAO;

public class PostDAO extends AbstractDAO<Post> {

    public PostDAO(){
        super(Post.class);
    }


    @Override
    public Object getId(Post obj) {
        return obj.getId();
    }
}
