package com.rca.stock.services.impl;

import com.rca.stock.dto.supplier.SupplierDto;
import com.rca.stock.dto.supplier.SupplierResponseDto;
import com.rca.stock.exception.SupplierNotFoundException;
import com.rca.stock.mapper.SupplierMapper;
import com.rca.stock.model.Supplier;
import com.rca.stock.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierServiceImplTest {

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Mock
    private SupplierRepository repository;

    @Mock
    private SupplierMapper supplierMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldCreateSupplier_ReturnSupplier() {
        SupplierDto supplierDto = SupplierDto.builder()
                .companyName("ABC Suppliers")
                .companyEmail("abc@example.com")
                .build();



        Supplier supplier = Supplier.builder()
                .companyName("ABC Suppliers")
                .companyEmail("abc@example.com")
                .build();


        when(supplierMapper.toSupplier(supplierDto)).thenReturn(supplier);
        when(repository.save(supplier)).thenReturn(supplier);

        Supplier supplierCreated= supplierService.createSupplier(supplierDto);

        assertNotNull(supplierCreated);
        assertEquals(supplierDto.getCompanyName(), supplierCreated.getCompanyName());
        assertEquals(supplierDto.getCompanyEmail(), supplierCreated.getCompanyEmail());

        verify(repository, times(1)).save(supplier);
    }

    @Test
    void should_return_all_suppliers() {
        List<Supplier> suppliers = new ArrayList<>();

        suppliers.add(Supplier.builder()
                .companyEmail("abc@example.com")
                .build()
        );

        when(repository.findAll()).thenReturn(suppliers);

        SupplierResponseDto mockSupplierDto = SupplierResponseDto.builder()
                .companyEmail("abc@example.com")
                .build();

        when(supplierMapper.toDto(any(Supplier.class))).thenReturn(mockSupplierDto);
        List<Supplier> supplierResponseDtos = supplierService.getAllSuppliers();

        assertEquals(suppliers.size(), supplierResponseDtos.size());

        verify(repository, times(1)).findAll();
    }


    @Test
    void should_return_supplierBy_id(){
        Integer id = 1;

        Supplier supplier = Supplier.builder()
                .companyEmail("abc@example.com")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(supplier));

        SupplierResponseDto mockSupplierDto = SupplierResponseDto.builder()
                .companyEmail("abc@example.com")
                .build();

        when(supplierMapper.toDto(any(Supplier.class))).thenReturn(mockSupplierDto);

        Supplier supplierDto = supplierService.getSupplierById(id);

        assertNotNull(supplierDto);
        assertEquals(supplierDto.getCompanyName(), supplierDto.getCompanyName());
        assertEquals(supplierDto.getCompanyEmail(), supplierDto.getCompanyEmail());

        verify(repository, times(1)).findById(id);
    }

    @Test
    void updateSupplier_should_return_updated_supplier() {
        Integer id = 1;

        Supplier supplier = Supplier.builder()
                .companyEmail("abc@example.com")
                .build();

        SupplierDto responseDto = SupplierDto.builder()
                .companyEmail("abc@gmail.com")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(supplier));
        when(repository.save(supplier)).thenReturn(supplier);



        Supplier updatedSupplier = supplierService.updateSupplier(id, responseDto);

        assertNotNull(updatedSupplier);
        assertEquals(responseDto.getCompanyName(), updatedSupplier.getCompanyName());
        assertEquals(responseDto.getCompanyEmail(), updatedSupplier.getCompanyEmail());

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(supplier);

    }


    @Test
    void updateSupplier_ShouldThrowException_WhenSupplierDoesNotExist(){
        Integer id = 1;
        SupplierDto supplierDto = SupplierDto.builder()
                .companyEmail("abc@example.com")
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SupplierNotFoundException.class, () -> supplierService.updateSupplier(id , supplierDto));

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Supplier.class));
    }

    @Test
    void deleteSupplier_should_delete_supplier() {
        Integer id = 1;
        Supplier supplier = new Supplier();
        when(repository.findById(id)).thenReturn(Optional.of(supplier));

        supplierService.deleteSupplier(id);

        verify(repository).findById(id);
        verify(repository).delete(supplier);
    }

    @Test
    void deleteSupplier_ShouldThrowException_WhenSupplierDoesNotExist(){
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(SupplierNotFoundException.class, () -> supplierService.deleteSupplier(id ));

        verify(repository).findById(id);
        verify(repository, never()).delete(any(Supplier.class));
    }


}