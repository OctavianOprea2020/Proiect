package com.fasttrackit.countriesapplication.model.transaction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {
    private long id;
    private String product;
    private TransactionType type;
    private double amount;
}
