package org.startupkit.social.spider;

import org.startupkit.core.dao.AbstractDAO;

public class SpiderDAO extends AbstractDAO<Spider> {

    public SpiderDAO(){
        super(Spider.class);
    }


    @Override
    public Object getId(Spider obj) {
        return obj.getId();
    }
}
