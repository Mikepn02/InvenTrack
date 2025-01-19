package com.rca.stock.services;

import com.rca.stock.dto.supplier.SupplierDto;
import com.rca.stock.model.Supplier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface SupplierService {


    Supplier createSupplier(SupplierDto supplierDto);

    List<Supplier> getAllSuppliers();

    Supplier getSupplierById(Integer id);

    Supplier updateSupplier(Integer id, SupplierDto supplierDto);

    void deleteSupplier(Integer id);
}
