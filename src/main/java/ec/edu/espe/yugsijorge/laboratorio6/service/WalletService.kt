package ec.edu.espe.yugsijorge.laboratorio6.service

import ec.edu.espe.yugsijorge.laboratorio6.dto.AccountDTO
import ec.edu.espe.yugsijorge.laboratorio6.model.Account
import ec.edu.espe.yugsijorge.laboratorio6.repository.WalletRepository

class WalletService(private val walletRepository: WalletRepository, private val riskClient: RiskClient) {
    fun createAccount(email: String, initialBalance: Double): AccountDTO {
        require(!(email == null || !email.contains("@"))) { "Correo inválido" }
        require(!(initialBalance <= 0)) { "Saldo inicial debe ser mayor a 0" }
        if (riskClient.isBlocked(email)) throw RuntimeException("Cliente bloqueado por riesgo")
        if (walletRepository.existsByOwnerEmail(email)) throw RuntimeException("Ya existe una cuenta con ese email")

        val account = Account(email, initialBalance)
        walletRepository.save(account)

        return AccountDTO(account.getId(), account.getBalance())
    }

    fun deposit(accountId: String?, amount: Double): Double {
        require(!(amount <= 0)) { "Monto inválido para depósito" }

        val account = walletRepository.findById(accountId)
        if (account == null) throw RuntimeException("La cuenta no existe")

        account.deposit(amount)
        walletRepository.save(account)

        return account.getBalance()
    }

    // ================= NUEVA FUNCIONALIDAD (RAMA FEATURE) =================
    fun withdraw(accountId: String?, amount: Double): Double {
        require(!(amount <= 0)) { "Monto inválido para retiro" }

        val account = walletRepository.findById(accountId)
        if (account == null) throw RuntimeException("La cuenta no existe")

        account.withdraw(amount) // En el modelo lanzará excepción si no hay fondos
        walletRepository.save(account)

        return account.getBalance()
    }
}