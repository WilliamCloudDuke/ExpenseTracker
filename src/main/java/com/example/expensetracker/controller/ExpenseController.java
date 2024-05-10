package com.example.expensetracker.controller;

import com.example.expensetracker.dto.ExpenseDto;
import com.example.expensetracker.service.ExpenseService;
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

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateExpense(@RequestBody ExpenseDto expenseDto) {
        expenseService.updateExpense(expenseDto);
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseDto> getAllExpenses() {
        return expenseService.getAllExpenses();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseDto getExpense(@PathVariable String id) {
        return expenseService.getExpense(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
    }

}
