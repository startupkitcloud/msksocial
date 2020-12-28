package org.startupkit.social.like;

import org.startupkit.notification.email.EmailService;
import org.startupkit.user.util.SecuredUser;

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
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/like")
	public void like(Like like)  throws Exception{
		likesService.like(like);
	}


	@SecuredUser
	@GET
	@Path("/listLikesMe/{idObject}/{typeObject}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<? extends Like> listLikesMe(final @PathParam("idObject") String idObject,
											final @PathParam("typeObject") String typeObject) throws Exception {
		return likesService.listLikesMe(idObject, typeObject);
	}


	@SecuredUser
	@GET
	@Path("/listILike/{idObject}/{typeObject}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<? extends Like> listILike(final @PathParam("idObject") String idObject, final @PathParam("typeObject") String typeObject) throws Exception {
		return likesService.listILike(idObject, typeObject);
	}
}
