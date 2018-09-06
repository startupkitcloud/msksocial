package com.mangobits.startupkit.social.spider;


import javax.ejb.Local;

@Local
public interface SpiderService {

    void goSpider() throws Exception;
}
