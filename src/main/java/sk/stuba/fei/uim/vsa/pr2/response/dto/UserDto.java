package sk.stuba.fei.uim.vsa.pr2.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends Dto{
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<CarResponse> cars;
}
