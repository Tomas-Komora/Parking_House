package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarTypeDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.UserDto;

public class CarTypeFactory implements ResponseFactory<CarType, CarTypeDto>{
    @Override
    public CarTypeDto transformToDto(CarType entity) {
        CarTypeDto carTypeDto = new CarTypeDto();
        carTypeDto.setId(entity.getId());
        carTypeDto.setName(entity.getTypeName());

        return carTypeDto;
    }

    @Override
    public CarType transformToEntity(CarTypeDto dto) {
        CarType carType = new CarType();
        carType.setId(dto.getId());
        carType.setTypeName(dto.getName());

        return carType;
    }
}
