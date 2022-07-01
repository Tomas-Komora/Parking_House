package sk.stuba.fei.uim.vsa.pr2.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.User;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<CarResponse> cars;

    public UserResponse(User user){
        this.id = user.getId();
        this.firstName = user.getName();
        this.lastName = user.getSurname();
        this.email = user.getEmail();

        List<Car> tmp = user.getCarList();
        List<CarResponse> finalList = new ArrayList<>();
        for (Car cpf:tmp) {
            finalList.add(new CarResponse(cpf));
        }
        this.cars = finalList;
    }
}
