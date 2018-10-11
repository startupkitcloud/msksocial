package com.mangobits.startupkit.social.group;

import com.mangobits.startupkit.core.dao.AbstractDAO;
import com.mangobits.startupkit.social.post.Post;

public class GroupDAO extends AbstractDAO<Group> {

    public GroupDAO() {
        super(Group.class);
    }


    @Override
    public Object getId(Group obj) {
        return obj.getId();
    }

}
