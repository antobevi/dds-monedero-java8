package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
  private BigDecimal saldo = new BigDecimal(0);
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo.equals(new BigDecimal(0));
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

    Validador validarLimiteDepositos = new ValidadorLimiteDepositos();
    validarLimiteDepositos.validar(this.cantidadDepositos(), saldo);

    this.agregarMovimiento(LocalDate.now(), cuanto, true);
  }

  public void sacar(BigDecimal cuanto) {

    Validador validarMontoNegativo = new ValidadorMontoNegativo();
    validarMontoNegativo.validar(cuanto, saldo);

    Validador validarLimiteExtraccion = new ValidadorLimiteExtraccion();
    validarLimiteExtraccion.validar(getSaldo().subtract(cuanto), saldo);

    Validador validarLimiteExtraccionDiaria = new ValidadorLimiteExtraccionDiaria();
    validarLimiteExtraccionDiaria.validar(cuanto, this.montoLimite());

    this.agregarMovimiento(LocalDate.now(), cuanto, false);
  }

  public BigDecimal montoLimite() {
    BigDecimal montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    return montoExtraidoHoy.subtract(new BigDecimal(1000));
  }

  public BigDecimal cantidadDepositos() {
    BigDecimal cantDepositos = new BigDecimal(getMovimientos().stream()
        .filter(movimiento -> movimiento.isDeposito()).count());
    return cantDepositos;
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
