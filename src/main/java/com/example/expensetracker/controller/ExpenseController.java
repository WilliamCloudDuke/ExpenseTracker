package com.example.expensetracker.controller;

import com.example.expensetracker.dto.ExpenseDto;
import com.example.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;


    @Operation(summary = "Add an Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid/Bad Request", content = @Content)})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addExpense(@RequestBody @Valid ExpenseDto expenseDto) {
        String expenseId = expenseService.addExpense(expenseDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(expenseId)
                .toUri();
        return ResponseEntity.created(location).build();
    }


    @Operation(summary = "Update an Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense Updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExpenseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid/Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Expense Not Found", content = @Content)})
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateExpense(@RequestBody ExpenseDto expenseDto) {
        expenseService.updateExpense(expenseDto);
    }


    @Operation(summary = "Get All Expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Expenses", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExpenseDto.class))})})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }


    @Operation(summary = "Get an Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Single Expense", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExpenseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Expense Not Found", content = @Content)
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseDto getExpense(@PathVariable String id) {
        return expenseService.getExpense(id);
    }

    @Operation(summary = "Delete an Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Expense Deleted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExpenseDto.class))}),
            @ApiResponse(responseCode = "404", description = "Expense Not Found", content = @Content)


    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
    }

}
