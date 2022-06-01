package ro.ubb.opera.web.converter;

import ro.ubb.opera.core.model.Venue;
import ro.ubb.opera.web.dto.VenueDto;

public class VenueConverter extends BaseConverter<Venue, VenueDto> {
    @Override
    public Venue convertDtoToModel(VenueDto venueDto) {
        Venue venue = new Venue();
        venue.setId(venueDto.getId());
        venue.setNumberOfSeats(venueDto.getNumberOfSeats());
        venue.setFloor(venueDto.getFloor());
        return venue;
    }

    @Override
    public VenueDto convertModelToDto(Venue venue) {
        VenueDto venueDto = new VenueDto(venue.getNumberOfSeats(), venue.getFloor());
        venueDto.setId(venue.getId());
        return venueDto;
    }
}
