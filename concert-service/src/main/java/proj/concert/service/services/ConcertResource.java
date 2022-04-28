package proj.concert.service.services;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.PerformerMapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
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

    @GET
    @Path("/concerts")
        public Response retrieveConcerts(){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            em.getTransaction().begin();
            TypedQuery<Concert> concertTypedQuery = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = concertTypedQuery.getResultList();
            if(concerts == null){return Response.status(Response.Status.NOT_FOUND).build();}

            Set<ConcertDTO> concertDTOS = new HashSet<>();
            for(Concert c : concerts){
                concertDTOS.add(ConcertMapper.toDTO(c));
            }
            return Response.ok(concertDTOS).build();

        }finally{
            if(em.getTransaction().isActive()){
                em.getTransaction().commit();
            }
            em.close();
        }
    }

    @GET
    @Path("/performers")
    public Response retrievePerformers(){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            em.getTransaction().begin();

            TypedQuery<Performer> performerTypedQuery = em.createQuery("select p from Performer p", Performer.class);
            List<Performer> performers = performerTypedQuery.getResultList();
            if(performers == null){ return Response.status(Response.Status.NOT_FOUND).build();}

            Set<PerformerDTO> performerDTOs = new HashSet<>();
            for (Performer p : performers) {
                performerDTOs.add(PerformerMapper.toDTO(p));
            }
            GenericEntity<Set<PerformerDTO>> retrievedPerformers = new GenericEntity<Set<PerformerDTO>>(performerDTOs) {};

            return Response.ok(retrievedPerformers).build();
        }finally{
            if(em.getTransaction().isActive()){
                em.getTransaction().commit();
            }
            em.close();
        }


    }

    @GET
    @Path("performers/{id}")
    public Response getPerformer(@PathParam("id") long id){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            em.getTransaction().begin();
            Performer performer = em.find(Performer.class, id );

            if(performer == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(PerformerMapper.toDTO(performer)).build();

        }finally{
            if(em.getTransaction().isActive()){
                em.getTransaction().commit();
            }
            em.close();

        }
    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries(){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Concert> concertTypedQuery = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = concertTypedQuery.getResultList();
            if (concerts == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Set<ConcertSummaryDTO> concertSummaries = new HashSet<>();
            for (Concert c : concerts) {
                concertSummaries.add(ConcertMapper.concertSummaryDTO(c));

            }
            em.close();
            return Response.ok(concertSummaries).build();
        } finally{
            if(em.getTransaction().isActive())
                em.getTransaction().commit();
        }


    }

}
