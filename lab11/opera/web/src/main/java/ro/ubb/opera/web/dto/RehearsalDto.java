package ro.ubb.opera.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RehearsalDto extends BaseDto {
    private Integer orchestraId;
    private Integer venueId;
}
