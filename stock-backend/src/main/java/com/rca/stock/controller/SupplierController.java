package com.rca.stock.controller;

import com.rca.stock.dto.supplier.SupplierDto;
import com.rca.stock.dto.supplier.SupplierResponseDto;
import com.rca.stock.mapper.SupplierMapper;
import com.rca.stock.model.Supplier;
import com.rca.stock.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierMapper mapper;


    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@RequestBody SupplierDto supplierDto) {
        Supplier createdSupplier = supplierService.createSupplier(supplierDto);
        return ResponseEntity.ok(mapper.toDto(createdSupplier));
    }

    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        List<SupplierResponseDto> supplierDtos = suppliers.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(supplierDtos, HttpStatus.OK);
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Integer supplierId) {
        Supplier supplier = supplierService.getSupplierById(supplierId);
        if (supplier != null) {
            return ResponseEntity.ok(supplier);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Integer supplierId, @RequestBody SupplierDto updatedSupplier) {
        Supplier supplier = supplierService.updateSupplier(supplierId, updatedSupplier);
        if (supplier != null) {
            return ResponseEntity.ok(supplier);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Integer supplierId) {
        supplierService.deleteSupplier(supplierId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
