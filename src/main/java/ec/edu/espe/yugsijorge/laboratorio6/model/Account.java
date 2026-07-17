package ec.edu.espe.yugsijorge.laboratorio6.model;

import java.util.UUID;

public class Account {
    private String id;
    private String ownerEmail;
    private double balance;

    public Account(String ownerEmail, double balance) {
        this.id = UUID.randomUUID().toString(); // Id aleatorio
        this.ownerEmail = ownerEmail;
        this.balance = balance;
    }

    public String getId() { return id; }
    public String getOwnerEmail() { return ownerEmail; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto a depositar debe ser mayor a 0");
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("El monto a retirar debe ser mayor a 0");
        if (this.balance < amount) throw new RuntimeException("Fondos insuficientes");
        this.balance -= amount;
    }
}