package com.example.ecommerce_clean.modules.product.application.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.example.ecommerce_clean.modules.product.application.dto.CreateProductRequest;
import com.example.ecommerce_clean.modules.product.application.dto.ProductResponse;
import com.example.ecommerce_clean.modules.product.application.dto.UpdateProductRequest;
import com.example.ecommerce_clean.modules.product.domain.entity.Product;
import com.example.ecommerce_clean.shared.domain.valueobject.Money;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "bigDecimalToMoney")
    Product toEntity(CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "bigDecimalToMoney")
    void updateEntity(UpdateProductRequest request, @MappingTarget Product product);

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "price", source = "price", qualifiedByName = "moneyToDouble")
    ProductResponse toResponse(Product product);
    
    @Named("bigDecimalToMoney")
    default Money bigDecimalToMoney(BigDecimal value) {
        return value != null ? new Money(value, "USD") : null;
    }
    
    @Named("moneyToDouble")
    default Double moneyToDouble(Money money) {
        return money != null ? money.amount().doubleValue() : null;
    }
}
