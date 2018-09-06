package com.mangobits.startupkit.social.spider.site;

import com.mangobits.startupkit.core.dao.AbstractDAO;

public class SiteDAO extends AbstractDAO<Site> {

    public SiteDAO(){
        super(Site.class);
    }


    @Override
    public Object getId(Site obj) {
        return obj.getId();
    }
}
