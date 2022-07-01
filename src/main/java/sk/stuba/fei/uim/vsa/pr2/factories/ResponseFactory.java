package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.response.dto.Dto;

public interface ResponseFactory <R, T extends Dto> {
    T transformToDto(R entity);
    R transformToEntity(T dto);
}
