package com.example.expensetracker.dto;

import com.example.expensetracker.model.ExpenseCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]+")
    private String id;

    @NotBlank
    //@Pattern(regexp = "[A-Za-z]")
    private String expenseName;

    private ExpenseCategory expenseCategory;

    @Min(value = 0)
    private BigDecimal expenseAmount;


}
