package net.dunice.mstool.mappers;

import net.dunice.mstool.DTO.request.RegisterUserRequest;
import net.dunice.mstool.DTO.response.LoginUserResponse;
import net.dunice.mstool.DTO.response.PublicUserResponse;
import net.dunice.mstool.DTO.response.PutUserResponse;
import net.dunice.mstool.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMappers {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "password")
    UserEntity registerUserRequestToUserEntity(RegisterUserRequest registerUserRequest);

    @Mapping(ignore = true, target = "token")
    LoginUserResponse toLoginUserResponse(UserEntity user);

    PublicUserResponse toPublicUserResponse(UserEntity user);

    PutUserResponse putUserRequestToPutUserResponse(UserEntity user);
}
