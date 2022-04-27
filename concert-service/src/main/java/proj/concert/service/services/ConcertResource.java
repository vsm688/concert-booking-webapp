package proj.concert.service.services;

import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;
import proj.concert.service.mapper.ConcertMapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {


    @GET
    @Path("/concerts/{id}")
    public Response getConcert(@PathParam("id") long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, id, LockModeType.PESSIMISTIC_READ);

            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(ConcertMapper.toDTO(concert)).build();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }
    }



}
