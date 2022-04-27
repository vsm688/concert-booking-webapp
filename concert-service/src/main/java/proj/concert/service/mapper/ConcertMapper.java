package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Concert;
import proj.concert.service.domain.Performer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertMapper {
    private ConcertMapper(){}
        public static ConcertDTO toDTO(Concert concert) {
            ConcertDTO cdto = new ConcertDTO(concert.getId(), concert.getTitle(), concert.getImage_name(), concert.getBlurb());
            List<PerformerDTO> performers = new ArrayList<>();
            for(Performer performer : concert.getPerformers()){
                performers.add(new PerformerDTO(performer.getId(), performer.getName(), performer.getImage_name(), performer.getGenre(), performer.getBlurb()));
            }
            List<LocalDateTime> dates = new ArrayList<>();
            for(LocalDateTime date : concert.getDates()){
                dates.add(date);
            }
            cdto.setDates(dates);
            cdto.setPerformers(performers);
            return cdto;
        }
}