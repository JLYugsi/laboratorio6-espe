package ec.edu.espe.yugsijorge.laboratorio6.repository;

import ec.edu.espe.yugsijorge.laboratorio6.model.Account;

public interface WalletRepository {
    void save(Account account);
    Account findById(String id);
    boolean existsByOwnerEmail(String email);
}