package ro.ubb.opera.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RehearsalsDto {
    private Set<RehearsalDto> rehearsals;
}
