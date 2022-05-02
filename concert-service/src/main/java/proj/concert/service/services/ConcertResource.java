package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.BookingMapper;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.PerformerMapper;
import proj.concert.service.mapper.SeatMapper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.awt.print.Book;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private PersistenceManager persistenceManager;

    public ConcertResource(){
        this.persistenceManager = PersistenceManager.instance();
    }

    public static final String AUTH_COOKIE = "auth";

    @GET
    @Path("/concerts/{id}")

    public Response getConcert(@PathParam("id") long id) {
        EntityManager em = persistenceManager.createEntityManager();
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
    public Response retrieveConcerts() {
        EntityManager em = persistenceManager.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Concert> concertTypedQuery = em.createQuery("select c from Concert c", Concert.class);
            List<Concert> concerts = concertTypedQuery.getResultList();
            if (concerts == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Set<ConcertDTO> concertDTOS = new HashSet<>();
            for (Concert c : concerts) {
                concertDTOS.add(ConcertMapper.toDTO(c));
            }
            return Response.ok(concertDTOS).build();

        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }
    }

    @GET
    @Path("/performers")
    public Response retrievePerformers() {
        EntityManager em = persistenceManager.createEntityManager();
        try {
            em.getTransaction().begin();

            TypedQuery<Performer> performerTypedQuery = em.createQuery("select p from Performer p", Performer.class);
            List<Performer> performers = performerTypedQuery.getResultList();
            if (performers == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            Set<PerformerDTO> performerDTOs = new HashSet<>();
            for (Performer p : performers) {
                performerDTOs.add(PerformerMapper.toDTO(p));
            }
            GenericEntity<Set<PerformerDTO>> retrievedPerformers = new GenericEntity<Set<PerformerDTO>>(performerDTOs) {
            };

            return Response.ok(retrievedPerformers).build();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();
        }


    }

    @GET
    @Path("performers/{id}")
    public Response getPerformer(@PathParam("id") long id) {
        EntityManager em = persistenceManager.createEntityManager();
        try {
            em.getTransaction().begin();
            Performer performer = em.find(Performer.class, id);

            if (performer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(PerformerMapper.toDTO(performer)).build();

        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
            em.close();

        }
    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertSummaries() {
        EntityManager em = persistenceManager.createEntityManager();
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
        } finally {
            if (em.getTransaction().isActive())
                em.getTransaction().commit();
        }
    }

    @POST
    @Path("/login")
    public Response login(UserDTO userLogInAttempt) {
        EntityManager em = persistenceManager.createEntityManager();
        try {
            em.getTransaction().begin();
            User user;
            List<Long> id;
            id = (List<Long>) em.createQuery("SELECT user.id FROM User user WHERE user.username = :username").setParameter("username", userLogInAttempt.getUsername()).getResultList();
            if(id.size() > 0){
                user = em.find(User.class, id.get(0));
                if(user.getUsername().equals(userLogInAttempt.getUsername()) && user.getPassword().equals(userLogInAttempt.getPassword())){
                    em.getTransaction().commit();
                    return Response.ok().cookie(makeCookie(user, em)).build();
                }
            }
            em.getTransaction().commit();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }finally{
            em.close();
        }

    }

    @GET
    @Path("/bookings")
    public Response getBookingsForUser(@CookieParam(AUTH_COOKIE) Cookie cookie){
        EntityManager em  = persistenceManager.createEntityManager();
        try{
            User user = this.getUserFromCookie(cookie, em);
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
             em.getTransaction().begin();
            List<Booking> bookings = em.createQuery("select b from Booking b where b.user.id = :userId", Booking.class)
                    .setParameter("userId", user.getId())
                    .getResultList();


            Set<BookingDTO> bookingDTOSet = new HashSet<>();
            for(Booking b: bookings){
                bookingDTOSet.add(BookingMapper.toDTO(b));
            }

            GenericEntity<Set<BookingDTO>> retrievedBookings= new GenericEntity<Set<BookingDTO>>(bookingDTOSet){};
            return Response.ok(retrievedBookings).build();


        }finally {
            em.close();
        }


    }

    @POST
    @Path("/bookings")
    public Response createBooking( BookingRequestDTO bookingRequestDTO,@CookieParam(AUTH_COOKIE) Cookie cookie){
        EntityManager em = persistenceManager.createEntityManager();
        try {
            User user = getUserFromCookie(cookie, em);
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            em.getTransaction().begin();
            LocalDateTime date = bookingRequestDTO.getDate();
            Concert concert;
            try{
            concert = em.createQuery("select c from Concert c where c.id= :concertId", Concert.class)
                    .setParameter("concertId", bookingRequestDTO.getConcertId())
                    .getSingleResult();

            }catch(NoResultException e){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            if(!concert.getDates().contains(date)){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            List<String> seatLabels = bookingRequestDTO.getSeatLabels();


            List<Seat> seats = new ArrayList<>();


        try {
            for (String labels : seatLabels) {
                Seat validSeat = em.createQuery("select s from Seat s where s.isBooked=false and s.date = :date and s.label = :label", Seat.class)
                        .setParameter("date", date)
                        .setParameter("label", labels)
                        .getSingleResult();
                seats.add(validSeat);

            }
        }catch(Exception e){
            return Response.status(Response.Status.FORBIDDEN).build();
        }

            Set<Seat> seatsSet = new HashSet<>();
            seatsSet.addAll(seats);

            Booking booking = new Booking(bookingRequestDTO.getConcertId(), bookingRequestDTO.getDate(), seatsSet);

            booking.setUser(user);

            for(Seat s: seats){
                s.setBooked(true);
            }
            em.persist(booking);
            em.getTransaction().commit();

            return Response.created(URI.create("/concert-service/bookings/" + booking.getId())).build();
        }finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
        }




    }

    @GET
    @Path("/bookings/{id}")
    public Response getBookingsById(@PathParam("id") Long id, @CookieParam(AUTH_COOKIE) Cookie cookie){

        EntityManager em = persistenceManager.createEntityManager();
        try{

            User user = getUserFromCookie(cookie, em);
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            em.getTransaction().begin();


            Booking booking = em.createQuery("select b from Booking b where b.id = :id", Booking.class)
                    .setParameter("id", id)
                    .getSingleResult();



            if(user.getId() != booking.getUser().getId()){
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            BookingDTO bookingDTO = BookingMapper.toDTO(booking);
            return Response.ok(bookingDTO).build();


        }finally {
            em.close();

        }
    }

    @GET
    @Path("/seats/{time}")
    public Response getSeats(@PathParam("time") LocalDateTimeParam localDateTimeParam, @QueryParam("status") String status){
        LocalDateTime localDateTime = localDateTimeParam.getLocalDateTime();
        EntityManager em = persistenceManager.createEntityManager();
        try{
            em.getTransaction().begin();
            List<Seat> seats;
            if(status.equals("Any")){
                seats = em.createQuery("select s from Seat s where s.date = :date", Seat.class)
                        .setParameter("date", localDateTime)
                        .getResultList();
            }
            else if(status.equals("Booked")){
                seats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = true", Seat.class)
                        .setParameter("date", localDateTime)
                        .getResultList();
            }
            else{
                seats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = false", Seat.class)
                        .setParameter("date", localDateTime)
                        .getResultList();
            }
            List<SeatDTO> seatsDTO = new ArrayList<>();
            for(Seat s :  seats){
                seatsDTO.add(SeatMapper.toDTO(s));
            }

            GenericEntity<List<SeatDTO>> listGenericEntity = new GenericEntity<List<SeatDTO>>(seatsDTO){};
            return Response.ok(listGenericEntity).build();
        }finally {
            em.close();
        }
    }


    private NewCookie makeCookie(User user, EntityManager em) {
        em.getTransaction().begin();
        em.lock(user, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        user.setSessionId(UUID.randomUUID());
        em.getTransaction().commit();
        return new NewCookie(AUTH_COOKIE, user.getSessionId().toString());
    }

    private User getUserFromCookie(Cookie cookie, EntityManager em){
        if(cookie == null){
            return null;
        }

        try{
            em.getTransaction().begin();
            User user = (User)em.createQuery("select u from User u where u.sessionId= :sessionId")
                    .setParameter("sessionId", UUID.fromString(cookie.getValue()))
                    .getSingleResult();

            em.getTransaction().commit();
            return user;
        }catch(NoResultException ignored){

        }
        return null;

    }


}
