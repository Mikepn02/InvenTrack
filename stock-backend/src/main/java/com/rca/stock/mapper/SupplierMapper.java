package com.rca.stock.mapper;

import com.rca.stock.dto.supplier.SupplierDto;
import com.rca.stock.dto.supplier.SupplierResponseDto;
import com.rca.stock.model.Order;
import com.rca.stock.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierMapper {

    public Supplier toSupplier(SupplierDto dto) {
        if (dto == null) {
            throw new NullPointerException("Supplier cannot be null");
        }

        return Supplier.builder()
                .companyName(dto.getCompanyName())
                .taxId(dto.getTaxId())
                .companyEmail(dto.getCompanyEmail())
                .companyHolder(dto.getCompanyHolder())
                .companyPhoneNumber(dto.getCompanyPhoneNumber())
                .build();
    }
    public SupplierResponseDto toDto(Supplier supplier) {
        if (supplier == null) {
            return null;
        }

        List<String> itemNames = supplier.getOrders().stream()
                .map(Order::getStockItem)
                .filter(stockItem -> stockItem != null)
                .map(stockItem -> stockItem.getName())
                .collect(Collectors.toList());

        return SupplierResponseDto.builder()
                .companyEmail(supplier.getCompanyEmail())
                .companyName(supplier.getCompanyName())
                .companyPhoneNumber(supplier.getCompanyPhoneNumber())
                .taxId(supplier.getTaxId())
                .companyHolder(supplier.getCompanyHolder())
                .itemsNames(itemNames)
                .build();
    }
}
