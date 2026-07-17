package ec.edu.espe.yugsijorge.laboratorio6.service

import ec.edu.espe.yugsijorge.laboratorio6.dto.AccountDTO
import ec.edu.espe.yugsijorge.laboratorio6.repository.WalletRepository
import ec.edu.espe.yugsijorge.laboratorio6.service.RiskClient

class WalletService (private val walletRepository: WalletRepository, private val riskClient: RiskClient) {

    fun createAccount(email: kotlin.String, initialBalance: kotlin.Double): AccountDTO {
        require(!(email == null || !email.contains("@"))) {"Correo inválido"}
        require(!(initialBalance <= 0)) {"Saldo inicial debe ser mayor a 0"}
        if (riskClient.isBlocked(email)) throw java.lang.RuntimeException("Cliente bloqueado por riesgo")
        if (walletRepository.existsByOwnerEmail(email)) throw java.lang.RuntimeException("Ya existe una cuenta con ese email")

        val account: ec.edu.espe.yugsijorge.laboratorio6.model.Account = ec.edu.espe.yugsijorge.laboratorio6.model.Account(email, initialBalance)
        walletRepository.save(account)

        return AccountDTO(account.getId(), account.getBalance())
    }

    fun deposit(accountId: kotlin.String?, amount: kotlin.Double): kotlin.Double {
        require(!(amount <= 0)) {"Monto inválido para depósito"}

        val account: ec.edu.espe.yugsijorge.laboratorio6.model.Account = walletRepository.findById(accountId)
        if (account == null) throw java.lang.RuntimeException("La cuenta no existe")

        account.deposit(amount)
        walletRepository.save(account)

        return account.getBalance()
    }

    // ================= NUEVA FUNCIONALIDAD (RAMA FEATURE) =================
    fun withdraw(accountId: kotlin.String?, amount: kotlin.Double): kotlin.Double {
        require(!(amount <= 0)) {"Monto inválido para retiro"}

        val account: ec.edu.espe.yugsijorge.laboratorio6.model.Account = walletRepository.findById(accountId)
        if (account == null) throw java.lang.RuntimeException("La cuenta no existe")

        account.withdraw(amount) // En el modelo lanzará excepción si no hay fondos
        walletRepository.save(account)

        return account.getBalance()
    }
}