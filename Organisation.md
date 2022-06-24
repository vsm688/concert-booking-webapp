1. Initially we discussed the domain model together and drew up a draft ERD. It took a few tries until we were
   able to come up with something that made sense. In terms of implementation we all worked on bits and pieces
   of it and put in equal amounts of work for everything. For the services, sbas780 was mainly responsible for
   Concerts, performers, PerformerIDs and Concert summaries and Bookings. hgra731 was mainly responsible for
   login/authentication and getConcertID. vmes688 was responsible for the subscribers part of the services and for
   working on the domain model classes. Overall/in general we all put in equal amounts of 
   work we would usually meet up and discuss the code together and the person who was responsible for something 
   would be the one who actually committed it with everyone present.

2. We used optimistic locking as well as version control, specifically, we did this for the seat class. We chose
   to do this because users are able to manipulate the state of a seat object when they make a booking (isbooked attribute), therefore, if 2 users attempt to book the same seats simultaneously it would cause concurrency issues, therefore we added a version to the seat class and when querying seats we used a lock type of OPTIMISTIC and FORCE_INCREMENT in order
   to ensure that another user would not be able to book the same seats before the initial users booking request goes through. We didn't use concurrency control anywhere else. This is because there is no other domain model object who's
   "state" is able to manipulated by a user other than seat.


3. For Concert performers we decided to use lazy loading, one reason for this is in the bookings page, only the concert   summaries are required, it wouldn't make sense to query performers from the database when they are only required in the
   homepage costing extra operations. Another notable feature about our domain model is that we use a composite key to uniquely identify a seat. This is because all seat labels across concerts are the same, thus we use date and label to uniquely identify seats for a particular concert.
   We added CascadeType.PERSIST to all owner classes such as Concert which stores its associated performers as well as Bookings which stores its' associated seats and this is because we persist the parent entity we want the child entities to be persisted
   . We did not add the REMOVE cascade type to these because we assumed performers can exist independently of concerts and also seats do not have to be a part of a booking, therefore it wouldn't make sense to delete a performer if a concert stopped existing and the same goes for bookings and seats. 
   Furthermore, in User class for it's associated bookings we added CascadeType.REMOVE
   , the reason for this is that a Booking is unique to a User and cannot exist without a user, therefore, if a user gets deleted we would want to remove the User's associated Bookings.
   we planned what the domain model would roughly look like by drawing an ERD, please view Revision 2 ERD which shows roughly how we intended the domain model to
   be structured. 