package com.fasttrackit.countriesapplication.controller.transaction;

import com.fasttrackit.countriesapplication.model.transaction.Transaction;
import com.fasttrackit.countriesapplication.model.transaction.TransactionType;
import com.fasttrackit.countriesapplication.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll(@RequestParam(required = false) String product, @RequestParam(required = false) TransactionType type,
                                    @RequestParam(required = false) Double minAmount, @RequestParam(required = false) Double maxAmount) {
        return transactionService.getAll(product, type, minAmount, maxAmount);
    }

    @PostMapping
    public Transaction add(@RequestBody Transaction transaction) {
        return transactionService.add(transaction);
    }

    @GetMapping("reports/type")
    public Map<TransactionType, List<Transaction>> reportByType() {
        return transactionService.getTransactionsByType();
    }

}
