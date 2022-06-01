package ro.ubb.opera.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrchestraDto extends BaseDto {
    private String name;
    private String conductor;
}
