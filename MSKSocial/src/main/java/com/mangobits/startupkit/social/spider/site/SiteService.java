package com.mangobits.startupkit.social.spider.site;


import com.mangobits.startupkit.core.status.SimpleStatusEnum;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SiteService {

    List<Site> listByStatus(SimpleStatusEnum status) throws Exception;
}
