package org.startupkit.social.post;

public class PostSearch {

    private String idUser;

    private String queryString;

    private Double lat;

    private Double log;

    private Integer page;

    private Integer pageItensNumber;

    private String idGroup;

    private String type;

    private String status;

    private String section;

    private String idObj;

    private String idUserCreator;

    public String getIdObj() {
        return idObj;
    }

    public void setIdObj(String idObj) {
        this.idObj = idObj;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }


    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getIdUserCreator() {
        return idUserCreator;
    }

    public void setIdUserCreator(String idUserCreator) {
        this.idUserCreator = idUserCreator;
    }

    public Integer getPageItensNumber() {
        return pageItensNumber;
    }

    public void setPageItensNumber(Integer pageItensNumber) {
        this.pageItensNumber = pageItensNumber;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
