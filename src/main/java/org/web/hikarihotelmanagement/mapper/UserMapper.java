package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.*;
import org.web.hikarihotelmanagement.dto.request.UpdateProfileRequest;
import org.web.hikarihotelmanagement.dto.response.UserResponse;
import org.web.hikarihotelmanagement.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "customerTier", expression = "java(user.getCustomerTier() != null ? user.getCustomerTier().getCode() : null)")
    UserResponse toUserResponse(User user);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateProfileRequest request, @MappingTarget User user);
}
