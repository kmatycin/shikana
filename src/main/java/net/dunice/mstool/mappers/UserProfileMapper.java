package net.dunice.mstool.mappers;

import net.dunice.mstool.DTO.response.UserProfileResponse;
import net.dunice.mstool.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(target = "username", source = "username")
    @Mapping(target = "nickname", source = "username")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    @Mapping(target = "bio", source = "bio")
    @Mapping(target = "role", source = "role")
    UserProfileResponse toProfileResponse(UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromProfile(UserProfileResponse profile, @MappingTarget UserEntity user);
} 