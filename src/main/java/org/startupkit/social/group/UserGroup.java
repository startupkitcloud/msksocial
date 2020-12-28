package org.startupkit.social.group;

public class UserGroup {

    private String idUser;

    private String nameUser;

    private String idGroup;

    private Boolean fgAdmin;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Boolean getFgAdmin() {
        return fgAdmin;
    }

    public void setFgAdmin(Boolean fgAdmin) {
        this.fgAdmin = fgAdmin;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
}
