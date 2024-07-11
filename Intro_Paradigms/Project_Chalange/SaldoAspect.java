import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.JoinPoint;

@Aspect
public class SaldoAspect {

    @Before("execution(* Conta.sacar(..)) && args(valor)")
    public void beforeSacar(JoinPoint joinPoint, double valor) throws Throwable {
        Conta conta = (Conta) joinPoint.getTarget();
        if (valor > conta.getSaldo()) {
            throw new RuntimeException("Saldo insuficiente!");
        }
    }

    @AfterThrowing(pointcut = "execution(* Conta.sacar(..))", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error) {
        System.err.println("Erro ao sacar da conta: " + joinPoint.getTarget());
        System.err.println("Mensagem de erro: " + error.getMessage());
    }
}

// Classe Base Conta
abstract class Conta {
    protected double saldo;

    public Conta(double saldoInicial) {
        this.saldo = saldoInicial;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    public void sacar(double valor) {
        saldo -= valor;
    }

    public abstract void extrato();
}

// Classe ContaCorrente
class ContaCorrente extends Conta {
    public ContaCorrente(double saldoInicial) {
        super(saldoInicial);
    }

    @Override
    public void extrato() {
        System.out.println("Extrato Conta Corrente: Saldo = R$ " + getSaldo());
    }
}

// Classe ContaSalario
class ContaSalario extends Conta {
    public ContaSalario(double saldoInicial) {
        super(saldoInicial);
    }

    @Override
    public void extrato() {
        System.out.println("Extrato Conta Salário: Saldo = R$ " + getSaldo());
    }
}

// Classe ContaPoupanca
class ContaPoupanca extends Conta {
    public ContaPoupanca(double saldoInicial) {
        super(saldoInicial);
    }

    @Override
    public void extrato() {
        System.out.println("Extrato Conta Poupança: Saldo = R$ " + getSaldo());
    }
}

// Classe principal Banco
public class Banco {
    public static void main(String[] args) {
        Conta contaCorrente = new ContaCorrente(1000.0);
        Conta contaSalario = new ContaSalario(500.0);
        Conta contaPoupanca = new ContaPoupanca(1500.0);

        try {
            contaCorrente.sacar(2000.0);
        } catch (RuntimeException e) {
            System.err.println("Erro capturado no main: " + e.getMessage());
        }

        try {
            contaSalario.sacar(1000.0);
        } catch (RuntimeException e) {
            System.err.println("Erro capturado no main: " + e.getMessage());
        }

        try {
            contaPoupanca.sacar(2000.0);
        } catch (RuntimeException e) {
            System.err.println("Erro capturado no main: " + e.getMessage());
        }
    }
}
