package com.mangobits.startupkit.social.spider.site;

import com.mangobits.startupkit.core.dao.SearchBuilder;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.List;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SiteServiceImpl implements SiteService  {


    @New
    @Inject
    private SiteDAO siteDAO;


    @Override
    public List<Site> listByStatus(SimpleStatusEnum status) throws Exception {
        return siteDAO.search(new SearchBuilder()
                .appendParam("status", SimpleStatusEnum.ACTIVE)
                .build());
    }
}