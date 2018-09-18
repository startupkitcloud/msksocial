package com.mangobits.startupkit.social.spider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mangobits.startupkit.core.status.SimpleStatusEnum;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name= "spider")
@Indexed
public class Spider {

    @Id
    @DocumentId
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;



    private String name;


    private String url;


    @Field
    @Enumerated(EnumType.STRING)
    private SimpleStatusEnum status;


    @Field
    private String type;


    @ElementCollection
    private List<String> tags;


    @ElementCollection
    private List<String> urlPatterns;


    private String idUserPostCreator;


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
}
