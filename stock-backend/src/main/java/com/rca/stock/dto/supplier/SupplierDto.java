package com.rca.stock.dto.supplier;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class SupplierDto {

    private String companyEmail;
    private String companyName;
    private String companyPhoneNumber;
    private String companyHolder;
    private String taxId;
}
