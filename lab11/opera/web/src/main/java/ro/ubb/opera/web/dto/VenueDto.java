package ro.ubb.opera.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VenueDto extends BaseDto {
    private Integer numberOfSeats;
    private Integer floor;
}
