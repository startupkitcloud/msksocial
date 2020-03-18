package com.mangobits.startupkit.social.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangobits.startupkit.core.exception.BusinessException;
import com.mangobits.startupkit.notification.email.EmailService;
import com.mangobits.startupkit.user.util.SecuredUser;
import com.mangobits.startupkit.ws.JsonContainer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Stateless
@Path("/likes")
public class LikesRestService {
	
	
	@EJB
	private EmailService emailService;
	
	
	
	@EJB
	private LikesService likesService;




	@SecuredUser
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Path("/like")
	public String like(Like like)  throws Exception{

		String resultStr = null;
		JsonContainer cont = new JsonContainer();

		try {

			likesService.like(like);
			cont.setDesc("OK");

		} catch (Exception e) {

			if(!(e instanceof BusinessException)){
				e.printStackTrace();
			}

			cont.setSuccess(false);
			cont.setDesc(e.getMessage());

			emailService.sendEmailError(e);
		}


		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);

		return resultStr;
	}



	@SecuredUser
	@GET
	@Path("/listLikesMe/{idObject}/{typeObject}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String listLikesMe(final @PathParam("idObject") String idObject, final @PathParam("typeObject") String typeObject) throws Exception {

		String resultStr;
		JsonContainer cont = new JsonContainer();

		try {
			List list = likesService.listLikesMe(idObject, typeObject);
			cont.setData(list);

		} catch (Exception e) {

			if(!(e instanceof BusinessException)){
				e.printStackTrace();
				emailService.sendEmailError(e);
			}

			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
		}

		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);

		return resultStr;
	}



	@SecuredUser
	@GET
	@Path("/listILike/{idObject}/{typeObject}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public String listILike(final @PathParam("idObject") String idObject, final @PathParam("typeObject") String typeObject) throws Exception {

		String resultStr;
		JsonContainer cont = new JsonContainer();

		try {
			List list = likesService.listILike(idObject, typeObject);
			cont.setData(list);

		} catch (Exception e) {

			if(!(e instanceof BusinessException)){
				e.printStackTrace();
				emailService.sendEmailError(e);
			}

			cont.setSuccess(false);
			cont.setDesc(e.getMessage());
		}

		ObjectMapper mapper = new ObjectMapper();
		resultStr = mapper.writeValueAsString(cont);

		return resultStr;
	}
}
