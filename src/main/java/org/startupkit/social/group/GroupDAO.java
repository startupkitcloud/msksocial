package org.startupkit.social.group;

import org.startupkit.core.dao.AbstractDAO;

public class GroupDAO extends AbstractDAO<Group> {

    public GroupDAO() {
        super(Group.class);
    }


    @Override
    public Object getId(Group obj) {
        return obj.getId();
    }

}
