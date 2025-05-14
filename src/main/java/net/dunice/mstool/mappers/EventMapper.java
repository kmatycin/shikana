package net.dunice.mstool.mappers;

import net.dunice.mstool.DTO.request.EventDto;
import net.dunice.mstool.DTO.response.EventResponseDto;
import net.dunice.mstool.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(EventDto dto);

    @Mapping(target = "createdBy", source = "createdBy.email")
    @Mapping(target = "updatedBy", source = "updatedBy.email")
    EventResponseDto toResponseDto(EventEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(EventDto dto, @MappingTarget EventEntity entity);
} 