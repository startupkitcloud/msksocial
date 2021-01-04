package org.startupkit.social.like;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.startupkit.core.annotation.MSKEntity;
import org.startupkit.core.annotation.MSKId;

import java.util.List;


@MSKEntity(name="likes")
public class Likes {

	
	@MSKId
	@BsonProperty("_id")
    protected String idOjbect;

	protected String typeObject;

	protected List<Like> listLikesMe;

	protected List<Like> listILike;
	
	public Likes(){
		
	}
	
	
	public Likes(String idOjbect){
		this.idOjbect = idOjbect;
	}


	public String getIdOjbect() {
		return idOjbect;
	}

	public void setIdOjbect(String idOjbect) {
		this.idOjbect = idOjbect;
	}

	public String getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(String typeObject) {
		this.typeObject = typeObject;
	}

	public List<Like> getListLikesMe() {
		return listLikesMe;
	}

	public void setListLikesMe(List<Like> listLikesMe) {
		this.listLikesMe = listLikesMe;
	}

	public List<Like> getListILike() {
		return listILike;
	}

	public void setListILike(List<Like> listILike) {
		this.listILike = listILike;
	}
}
