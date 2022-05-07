package proj.concert.service.mapper;
import proj.concert.common.dto.PerformerDTO;
import proj.concert.service.domain.Performer;

//A class which maps a performer to a performerDTO

public class PerformerMapper {

    private PerformerMapper() {
    }

    /**
     * Maps a domain model performer to its relative DTO class
     */
    public static PerformerDTO toDTO(Performer p) {
        return new PerformerDTO(p.getId(), p.getName(), p.getImage_name(), p.getGenre(), p.getBlurb());
    }
}