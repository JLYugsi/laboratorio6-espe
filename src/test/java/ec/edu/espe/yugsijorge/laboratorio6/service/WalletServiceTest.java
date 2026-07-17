package ec.edu.espe.yugsijorge.laboratorio6.service;

import ec.edu.espe.yugsijorge.laboratorio6.dto.AccountDTO;
import ec.edu.espe.yugsijorge.laboratorio6.model.Account;
import ec.edu.espe.yugsijorge.laboratorio6.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    private WalletRepository walletRepository;
    private RiskClient riskClient;
    private WalletService walletService;

    private final String EMAIL_JORGE = "jorge.yugsi@espe.edu.ec";

    @BeforeEach
    void setUp() {
        walletRepository = mock(WalletRepository.class);
        riskClient = mock(RiskClient.class);
        walletService = new WalletService(walletRepository, riskClient);
    }

    @Test
    void test1_CreateAccount_Success() {
        when(riskClient.isBlocked(EMAIL_JORGE)).thenReturn(false);
        when(walletRepository.existsByOwnerEmail(EMAIL_JORGE)).thenReturn(false);

        AccountDTO dto = walletService.createAccount(EMAIL_JORGE, 100.0);

        assertNotNull(dto);
        assertEquals(100.0, dto.getBalance());
        verify(walletRepository).save(any(Account.class));
    }

    @Test
    void test2_CreateAccount_InvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.createAccount("correo_invalido", 100.0);
        });
        verifyNoInteractions(riskClient, walletRepository);
    }

    @Test
    void test3_Deposit_AccountNotFound() {
        when(walletRepository.findById("12345")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            walletService.deposit("12345", 50.0);
        });
    }

    @Test
    void test4_Deposit_SuccessWithCaptor() {
        String id = "67890";
        Account mockAccount = new Account(EMAIL_JORGE, 50.0);
        when(walletRepository.findById(id)).thenReturn(mockAccount);
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        double newBalance = walletService.deposit(id, 30.0);

        assertEquals(80.0, newBalance);
        verify(walletRepository).save(captor.capture());
        assertEquals(80.0, captor.getValue().getBalance());
    }

    // ================= PRUEBAS DE LA RAMA FEATURE =================

    @Test
    void test5_Withdraw_Success() {
        String id = "67890";
        Account mockAccount = new Account(EMAIL_JORGE, 100.0);
        when(walletRepository.findById(id)).thenReturn(mockAccount);

        double newBalance = walletService.withdraw(id, 40.0);

        assertEquals(60.0, newBalance);
        verify(walletRepository).save(any(Account.class));
    }

    @Test
    void test6_Withdraw_InsufficientFunds() {
        String id = "67890";
        Account mockAccount = new Account(EMAIL_JORGE, 50.0); // Solo tiene 50
        when(walletRepository.findById(id)).thenReturn(mockAccount);

        assertThrows(RuntimeException.class, () -> {
            walletService.withdraw(id, 100.0); // Intenta retirar 100
        });
        verify(walletRepository, never()).save(any(Account.class));
    }
}