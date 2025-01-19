package com.rca.stock.services.impl;

import com.rca.stock.dto.supplier.SupplierDto;
import com.rca.stock.exception.SupplierNotFoundException;
import com.rca.stock.mapper.SupplierMapper;
import com.rca.stock.model.Supplier;
import com.rca.stock.repository.SupplierRepository;
import com.rca.stock.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierMapper mapper;

    public Supplier createSupplier(SupplierDto supplierDto) {
        return supplierRepository.save(mapper.toSupplier(supplierDto));
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Integer id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));
        return supplier;
    }

    public Supplier updateSupplier(Integer id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));
        existingSupplier.setCompanyName(supplierDto.getCompanyName());
        existingSupplier.setCompanyEmail(supplierDto.getCompanyEmail());
        Supplier supplier = supplierRepository.save(existingSupplier);

        return supplier;
    }

    public void deleteSupplier(Integer id) {

        Supplier supplier = supplierRepository.findById(id)
                        .orElseThrow(() -> new SupplierNotFoundException("Supplier not found"));
        supplierRepository.delete(supplier);
    }
}
