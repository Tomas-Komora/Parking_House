package sk.stuba.fei.uim.vsa.pr2.response.dtoInput;

import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

import java.util.List;
@Data
@NoArgsConstructor
public class UserInputDto extends Dto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<CarInputDto> cars;
}
