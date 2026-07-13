package com.example.bankcards.mapper;

import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct маппер для преобразования сущностей {@link com.example.bankcards.entity.User}
 * в DTO {@link UserResponse}. Поле password исключено из маппинга.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
