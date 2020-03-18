package com.mangobits.startupkit.social.spider;


import com.mangobits.startupkit.core.status.SimpleStatusEnum;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SpiderService {

    Spider load(String id) throws Exception;

    void changeStatus(String id) throws Exception;

    void goSpider() throws Exception;

    List<Spider> listByStatus(SimpleStatusEnum status) throws Exception;

    void save(Spider spider) throws Exception;

    List<Spider> listAll() throws Exception;
}
