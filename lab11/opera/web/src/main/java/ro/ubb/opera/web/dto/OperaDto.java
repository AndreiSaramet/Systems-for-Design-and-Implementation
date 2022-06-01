package ro.ubb.opera.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperaDto extends BaseDto {
    private String title;
    private String language;
    private Integer composerId;
}
