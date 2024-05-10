package com.example.expensetracker.service;

import com.example.expensetracker.dto.ExpenseDto;
import com.example.expensetracker.exception.ExpenseNotFoundException;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;


    public String addExpense(ExpenseDto expenseDto) {
        Expense expense = mapFromDto(expenseDto);
        return expenseRepository.insert(expense).getId();
    }


    public void updateExpense(ExpenseDto expenseDto) {
        Expense expense = mapFromDto(expenseDto);
        //Fetch the expense from the DB
        Expense savedExpense = expenseRepository.findById(expenseDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("cannot find expense by Id %s", expense.getId())));
        expenseRepository.save(updateExpense(savedExpense, expenseDto));
    }

    public ExpenseDto getExpense(String id) {
        Expense savedExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(String.format("cannot find expense by Id %s", id)));
        return mapToDto(savedExpense);
    }


    public List<ExpenseDto> getAllExpenses() {
        List<Expense> list = expenseRepository.findAll();
        return list.stream().map(this::mapToDto).toList();
    }


    public void deleteExpense(String id) {
        //Fetch the Expense from the DB
        Expense savedExpense = expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(String.format("cannot find expense by Id %s", id)));
        expenseRepository.deleteById(id);
    }

    private ExpenseDto mapToDto(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .expenseName(expense.getExpenseName())
                .expenseCategory(expense.getExpenseCategory())
                .expenseAmount(expense.getExpenseAmount())
                .build();
    }


    private Expense updateExpense(Expense savedExpense, ExpenseDto expenseDto) {
        savedExpense.setExpenseName(expenseDto.getExpenseName());
        savedExpense.setExpenseCategory(expenseDto.getExpenseCategory());
        savedExpense.setExpenseAmount(expenseDto.getExpenseAmount());
        return savedExpense;
    }


    private Expense mapFromDto(ExpenseDto expense) {
        return Expense.builder()
                .id(expense.getId())
                .expenseName(expense.getExpenseName())
                .expenseCategory(expense.getExpenseCategory())
                .expenseAmount(expense.getExpenseAmount())
                .build();
    }
}
