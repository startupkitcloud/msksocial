package org.startupkit.social.spider;

import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;
import org.startupkit.core.status.SimpleStatusEnum;

import java.util.Date;
import java.util.List;

@MSKEntity(name= "spider")
public class Spider {

    @MSKId
    private String id;

    private String name;

    private String url;

    private String urlBase;

    private SimpleStatusEnum status;

    private String type;

    private List<String> tags;

    private List<String> urlPatterns;

    private String idUserPostCreator;

    private Date creationDate;

    public Spider() {
    }


    public Spider(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SimpleStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SimpleStatusEnum status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }


    public String getIdUserPostCreator() {
        return idUserPostCreator;
    }

    public void setIdUserPostCreator(String idUserPostCreator) {
        this.idUserPostCreator = idUserPostCreator;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
