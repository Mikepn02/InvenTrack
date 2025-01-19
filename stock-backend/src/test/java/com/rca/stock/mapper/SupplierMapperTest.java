package com.rca.stock.mapper;

import com.rca.stock.dto.supplier.SupplierDto;
import com.rca.stock.dto.supplier.SupplierResponseDto;
import com.rca.stock.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplierMapperTest {

    private SupplierMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SupplierMapper();
    }

    @Test
    void should_map_supplierDto_to_supplier(){
        SupplierDto supplierDto = SupplierDto.builder()
                .companyName("ABC Supplies")
                .companyEmail("abc@gmail.com")
                .build();


        Supplier supplier = mapper.toSupplier(supplierDto);

        assertNotNull(supplier);
        assertEquals(supplierDto.getCompanyName(), supplier.getCompanyName());
        assertEquals(supplierDto.getCompanyEmail(), supplier.getCompanyEmail());
    }

    @Test
    void should_thow_null_pointer_exception_when_dto_is_null(){
        var exp = assertThrows(NullPointerException.class, () -> mapper.toSupplier(null));
        assertEquals("Supplier cannot be null", exp.getMessage());
    }

    @Test
    void should_map_supplier_to_supplierDto(){
        Supplier supplier = Supplier.builder()
                .companyName("ABC Supplies")
                .companyEmail("abc@gmail.com")
                .build();

        SupplierResponseDto dto = mapper.toDto(supplier);

        assertNotNull(dto);
        assertEquals(supplier.getCompanyName(), dto.getCompanyName());
        assertEquals(supplier.getCompanyEmail(), dto.getCompanyEmail());
    }

}