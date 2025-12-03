package org.web.hikarihotelmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.web.hikarihotelmanagement.dto.request.CustomerTierRequest;
import org.web.hikarihotelmanagement.dto.response.CustomerTierDetailResponse;
import org.web.hikarihotelmanagement.entity.CustomerTier;

@Mapper(componentModel = "spring")
public interface CustomerTierMapper {
    
    @Mapping(target = "userCount", expression = "java(tier.getUsers() != null ? (long) tier.getUsers().size() : 0L)")
    CustomerTierDetailResponse toDetailResponse(CustomerTier tier);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tierOrder", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CustomerTier toEntity(CustomerTierRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tierOrder", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(CustomerTierRequest request, @MappingTarget CustomerTier tier);
}
