package com.fasttrackit.countriesapplication.service.transaction;

import com.fasttrackit.countriesapplication.model.transaction.Transaction;
import com.fasttrackit.countriesapplication.model.transaction.TransactionType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();
    private static long id = 0;

    public List<Transaction> getAll(String product, TransactionType type, Double minAmount, Double maxAmount) {
        Stream<Transaction> stream = transactions.stream();
        if (product != null) {
            stream = stream.filter(transaction -> transaction.getProduct().equals(product));
        }
        if (type != null) {
            stream = stream.filter(transaction -> transaction.getType().equals(type));
        }
        if (minAmount != null) {
            stream = stream.filter(transaction -> transaction.getAmount() >= minAmount);
        }
        if (maxAmount != null) {
            stream = stream.filter(transaction -> transaction.getAmount() <= maxAmount);
        }
        return stream.toList();
    }

    public Transaction add(Transaction transaction) {
        Transaction newTransaction = Transaction.builder()
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .product(transaction.getProduct())
                .id(id++).build();
        transactions.add(newTransaction);
        return newTransaction;
    }

    public Map<TransactionType, List<Transaction>> getTransactionsByType() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getType));
    }
}
