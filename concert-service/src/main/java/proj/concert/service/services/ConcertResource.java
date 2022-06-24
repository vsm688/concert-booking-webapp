package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.BookingMapper;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.PerformerMapper;
import proj.concert.service.mapper.SeatMapper;
import proj.concert.service.util.TheatreLayout;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.awt.print.Book;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private PersistenceManager persistenceManager;

    // Map that holds each concert date with its associated subscribers ( contained in the SubTuple class ).
    private static Map<LocalDateTime, ArrayList<SubTuple>> subs = new ConcurrentHashMap<>();


    public ConcertResource(){
        this.persistenceManager = PersistenceManager.instance();
    }

    public static final String AUTH_COOKIE = "auth";

    //This method returns a concert with a specific id
    @GET
    @Path("/concerts/{id}")
    public Response getConcert(@PathParam("id") long id) {
        EntityManager em = persistenceManager.createEntityManager();
        try {
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, id);
            //find a concert in the concert class with the matching id
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
            List<Concert> concerts = concertTypedQuery.getResultList(); // Gets the concerts
            if (concerts == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
                // If concerts list null, return 404.
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
            List<Performer> performers = performerTypedQuery.getResultList(); // Gets the Performers
            if (performers == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
                // If performers list empty, returns 404.
            }

            Set<PerformerDTO> performerDTOs = new HashSet<>();
            for (Performer p : performers) {
                performerDTOs.add(PerformerMapper.toDTO(p));
                // Users the PerformerMapper to convert Performer objects in to performerDTOs.
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
            // Finds the performer by the Id.

            if (performer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
                //If performer not found, returns 404.
            }
            return Response.ok(PerformerMapper.toDTO(performer)).build();
            // Uses PerformerMapper to convert the Performer object to performerDTO.

        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

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
                //If concerts , return 404.
            }
            Set<ConcertSummaryDTO> concertSummaries = new HashSet<>();
            for (Concert c : concerts) {
                concertSummaries.add(ConcertMapper.concertSummaryDTO(c));
                // Uses concertMapper to map the Concert objects to concertDTO.
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
            // I used a query which returned an array which would always contain 1 or no items as to avoid an error
            if(id.size() > 0){
                user = em.find(User.class, id.get(0));
                //Find the user associated with the ID
                if(user.getUsername().equals(userLogInAttempt.getUsername()) && user.getPassword().equals(userLogInAttempt.getPassword())){
                    em.getTransaction().commit();
                    return Response.ok().cookie(makeCookie(user, em)).build();
                }
            }
            //If there's nothing in the array, it means there isn't a user associated that matches the username and password
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
                //If user not found, returns 401.
            }
             em.getTransaction().begin();
            List<Booking> bookings = em.createQuery("select b from Booking b where b.user.id = :userId", Booking.class)
                    .setParameter("userId", user.getId())
                    .getResultList();


            Set<BookingDTO> bookingDTOSet = new HashSet<>();
            for(Booking b: bookings){
                bookingDTOSet.add(BookingMapper.toDTO(b));
                // Uses BookingMapper to convert Booking objects in to bookingDTOs
            }

            GenericEntity<Set<BookingDTO>> retrievedBookings= new GenericEntity<Set<BookingDTO>>(bookingDTOSet){};
            em.getTransaction().commit();
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
            em.getTransaction().begin();

            User user = getUserFromCookie(cookie, em);
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
                //If user not found, returns 401.
            }

            LocalDateTime date = bookingRequestDTO.getDate();
            Concert concert;
            try{ //Gets concert by Id.
            concert = em.createQuery("select c from Concert c where c.id= :concertId", Concert.class)
                    .setParameter("concertId", bookingRequestDTO.getConcertId())
                    .getSingleResult();

            }catch(Exception e){
                return Response.status(Response.Status.BAD_REQUEST).build();
                //If Concert not found, returns 400.
            }

            if(!concert.getDates().contains(date)){
                return Response.status(Response.Status.BAD_REQUEST).build();
                //If concert.dates doesnt contain the corresponding date, returns 400.
            }

            List<String> seatLabels = bookingRequestDTO.getSeatLabels();


            List<Seat> seats = new ArrayList<>();


        try {
            for (String labels : seatLabels) {
                Seat validSeat = em.createQuery("select s from Seat s where s.isBooked=false and s.date = :date and s.label = :label", Seat.class)
                        .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
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

            LocalDateTime datet = bookingRequestDTO.getDate();

            List<Seat> availableSeats = em.createQuery("select seat from Seat seat where seat.isBooked = false and seat.date = :datet", Seat.class)
                    .setParameter("datet",date)
                    .getResultList();
            // After a booking is made, notify all subscribers who have subscribed to a Concert on a specific date.
            notifySubscribersIfLessThanPrecentageBooked (availableSeats.size() ,datet);
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
            em.getTransaction().begin();
            User user = getUserFromCookie(cookie, em);
            if(user == null){
                return Response.status(Response.Status.UNAUTHORIZED).build();
                //If user not found, returns 401.
            }
            Booking booking;
            try {
                // Gets booking by id.
                booking = em.createQuery("select b from Booking b where b.id = :id", Booking.class)
                        .setParameter("id", id)
                        .getSingleResult();
            }catch(Exception e){
                return Response.status(Response.Status.NOT_FOUND).build();
                //If booking not found, return 404 response.
            }
            if(user.getId() != booking.getUser().getId()){
                //If the user's id does not match with the book's user id, return 403 response.
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
            if(status.equals("Any")){ //Gets all books with the corresponding date
                seats = em.createQuery("select s from Seat s where s.date = :date", Seat.class)
                        .setParameter("date", localDateTime)
                        .getResultList();
            }
            else if(status.equals("Booked")){//Gets all booked books with the corresponding date
                seats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = true", Seat.class)
                        .setParameter("date", localDateTime)
                        .getResultList();
            }
            else{ //Gets all unbooked books with the corresponding date
                seats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = false", Seat.class)
                        .setParameter("date", localDateTime)
                        .getResultList();
            }
            List<SeatDTO> seatsDTO = new ArrayList<>();
            for(Seat s :  seats){
                seatsDTO.add(SeatMapper.toDTO(s));
                // Uses SeatMapper to map Seat objects to SeatDTOs
            }

            GenericEntity<List<SeatDTO>> listGenericEntity = new GenericEntity<List<SeatDTO>>(seatsDTO){};
            return Response.ok(listGenericEntity).build();
        }finally {
            em.close();
        }
    }
    @POST
    @Path("/subscribe/concertInfo")
    public void concertSubscription (@Suspended AsyncResponse response, @CookieParam(AUTH_COOKIE) Cookie c, ConcertInfoSubscriptionDTO concertinDTO){
        EntityManager em = persistenceManager.createEntityManager();

        User subscriber = getUserFromCookie(c,em);
        if (subscriber == null){
            response.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        try{
            em.getTransaction().begin();
            Concert concert;
            Long lookUpID = concertinDTO.getConcertId();
            LocalDateTime dateTime = concertinDTO.getDate();

            try{
                concert = em.createQuery("select concert from Concert concert where concert.id = :lookUpID",Concert.class)
                        .setParameter("lookUpID", lookUpID)
                        .getSingleResult();;
            }
            catch(Exception e){
                response.resume(Response.status(400).build());
                return;
            }

            if (!concert.getDates().contains(dateTime)){
                response.resume(Response.status(400).build());
                return;
            }

            SubTuple st = new SubTuple(response,concertinDTO);
            // if a date exists return its associated arraylist otherwise return a new empty arraylist.
            ArrayList<SubTuple> subArr = subs.getOrDefault(concertinDTO.getDate(),new ArrayList<>());
            System.out.println("this is the array here!" + subArr);
            subArr.add(st);

            // After adding tuples to the array push this array using the concert date as a key.
            subs.put(concertinDTO.getDate(),subArr);
            System.out.println("this is subs map" + subs);
            em.getTransaction().commit();
        }

        finally {
            em.close();
        }

    }



    public void notifySubscribersIfLessThanPrecentageBooked (int seatsLeft, LocalDateTime date){

            try{
                ArrayList<SubTuple> subscribers =  subs.get(date);
                // return if there are no subscribers associated with a date ( concert )
                if (subscribers == null){
                    return;
                }
                System.out.println("this is seats" + seatsLeft);
                System.out.println("this is per row" + TheatreLayout.NUM_SEATS_PER_ROW);
                System.out.println("this is rows" + TheatreLayout.NUM_ROWS);

                // calculate percentage of seats that are left, convert to int to compare with the each subscriber.
                double d = ((double) seatsLeft) / (double) (TheatreLayout.NUM_SEATS_PER_ROW * TheatreLayout.NUM_ROWS ) * 100.00;
                int d2 = (int) d;
                // Iterate through subscribers, if the percentage of a concert booked is less than the percentage they mentioned
                // then send a notification to the subscriber (resume async response ).
                for (SubTuple s : subscribers){

                    if (d2 <= s.getPercentageBooked()){

                        s.getResponse().resume(Response.ok(new ConcertInfoNotificationDTO(seatsLeft)).build());

                    }
                }
            }
            catch (Exception e){
                System.out.println("heres the error :( " + e);
            }
    }


    //Helper method which returns a cookie
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
            //If cookie is null, the code should break.
        }
        UUID sessionId = UUID.fromString(cookie.getValue());
        //Gets the value from the given cookie then coverts to a UUID
        try{
            User user = (User)em.createQuery("select u from User u where u.sessionId= :sessionId")
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();
            return user;
        }catch(Exception ignored){
            //If results not found, passes then returns null.
        }
        return null;

    }


}
