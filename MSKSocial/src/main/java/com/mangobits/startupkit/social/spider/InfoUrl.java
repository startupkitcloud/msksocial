package com.mangobits.startupkit.social.spider;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Embeddable;

@Indexed
@Embeddable
public class InfoUrl {

	@Field
	private String url;
	
	
	private String urlPhoto;
	
	
	private String title;
	
	
	private String desc;

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlPhoto() {
		return urlPhoto;
	}

	public void setUrlPhoto(String urlFoto) {
		this.urlPhoto = urlFoto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titulo) {
		this.title = titulo;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String descricao) {
		this.desc = descricao;
	}
}
