package com.rca.stock.model;

import com.rca.stock.enums.PaymentTerms;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "company_email")
    private String companyEmail;
    @Column(name = "company_phone_number")
    private String companyPhoneNumber;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_holder")
    private String companyHolder;
    @Column(name = "tin_number")
    private String taxId;
    @OneToMany(mappedBy = "supplier")
    private List<Order> orders;
}
