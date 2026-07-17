package ec.edu.espe.yugsijorge.laboratorio6.dto;

public class AccountDTO {
    private String accountId;
    private double balance;

    public AccountDTO(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }
}