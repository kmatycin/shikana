package net.dunice.mstool.mapper;

import net.dunice.mstool.DTO.response.PilotResponse;
import net.dunice.mstool.entity.PilotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PilotMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nickname", source = "nickname")
    @Mapping(target = "realName", source = "realName")
    @Mapping(target = "team", source = "team")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "discipline", source = "discipline")
    @Mapping(target = "licenseLevel", source = "licenseLevel")
    @Mapping(target = "titles", source = "titles")
    @Mapping(target = "seasonWins", source = "seasonWins")
    @Mapping(target = "totalWins", source = "totalWins")
    @Mapping(target = "cars", source = "cars")
    @Mapping(target = "photoUrl", source = "photoUrl")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "status", source = "status")
    PilotResponse toResponse(PilotEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nickname", source = "nickname")
    @Mapping(target = "realName", source = "realName")
    @Mapping(target = "team", source = "team")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "number", source = "number")
    @Mapping(target = "age", source = "age")
    @Mapping(target = "discipline", source = "discipline")
    @Mapping(target = "licenseLevel", source = "licenseLevel")
    @Mapping(target = "titles", source = "titles")
    @Mapping(target = "seasonWins", source = "seasonWins")
    @Mapping(target = "totalWins", source = "totalWins")
    @Mapping(target = "cars", source = "cars")
    @Mapping(target = "photoUrl", source = "photoUrl")
    @Mapping(target = "isActive", source = "isActive")
    @Mapping(target = "status", source = "status")
    void updateEntityFromResponse(PilotResponse response, @MappingTarget PilotEntity entity);
} 