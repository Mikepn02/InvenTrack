package com.rca.stock.repository;

import com.rca.stock.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findByCompanyName(String name);
}
