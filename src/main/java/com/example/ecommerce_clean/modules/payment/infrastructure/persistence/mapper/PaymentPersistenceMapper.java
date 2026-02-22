package com.example.ecommerce_clean.modules.payment.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.ecommerce_clean.modules.payment.domain.entity.Payment;
import com.example.ecommerce_clean.modules.payment.infrastructure.persistence.entity.PaymentJpaEntity;

@Mapper(componentModel = "spring")
public interface PaymentPersistenceMapper {

    @Mapping(source = "order.id", target = "orderId")
    Payment toDomain(PaymentJpaEntity entity);

    @Mapping(target = "order", ignore = true)
    PaymentJpaEntity toJpaEntity(Payment domain);
}
