package com.mangobits.startupkit.social.spider;


import com.mangobits.startupkit.core.status.SimpleStatusEnum;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SpiderService {

    void goSpider() throws Exception;

    List<Spider> listByStatus(SimpleStatusEnum status) throws Exception;
}
