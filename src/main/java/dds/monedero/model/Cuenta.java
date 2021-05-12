package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.SaldoMenorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
  private BigDecimal saldo = new BigDecimal(0);
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo.equals(0);
  }

  public Cuenta(BigDecimal montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(BigDecimal cuanto) {

    Validador validarMontoNegativo = new ValidadorMontoNegativo();
    validarMontoNegativo.validar(cuanto, saldo);

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    this.agregarMovimiento(LocalDate.now(), cuanto, true);
  }

  public void sacar(BigDecimal cuanto) {

    Validador validarMontoNegativo = new ValidadorMontoNegativo();
    validarMontoNegativo.validar(cuanto, saldo);

    if (getSaldo().subtract(cuanto).signum() == -1) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }

    BigDecimal montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    BigDecimal limitePorDia = new BigDecimal(1000);
    BigDecimal montoLimite = montoExtraidoHoy.subtract(limitePorDia);
    if (cuanto.max(montoLimite) == cuanto) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + montoLimite);
    }

    this.agregarMovimiento(LocalDate.now(), cuanto, false);
  }

  public void agregarMovimiento(LocalDate fecha, BigDecimal cuanto, boolean esDeposito) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, esDeposito);
    movimientos.add(movimiento);
    this.modificarSaldo(movimiento);
  }

  public void modificarSaldo(Movimiento movimiento) {
    saldo = movimiento.calcularValor(this);
  }

  public BigDecimal getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .map(movimiento -> movimiento.getMonto())
        .reduce(new BigDecimal(0),(elementoA,elementoB) -> elementoA.add(elementoB));
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

}
