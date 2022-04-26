package proj.concert.service.services;

import proj.concert.service.domain.Concert;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {


    @GET
    @Path("/concerts/{id}")
    public Response retrieveConcert(@PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        Concert concert;
        try {
            em.getTransaction().begin();
            concert = em.find(Concert.class,id);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        if (concert != null){
            return Response.ok(concert).build();
        }
        return Response.status(404).build();

    }


}
