package dds.monedero.model;

import dds.monedero.exceptions.SaldoMenorException;

import java.math.BigDecimal;

public class ValidadorLimiteExtraccion implements Validador {

  public void validar(BigDecimal monto, BigDecimal montoMaximo) {
    if(monto.signum() == -1) {
      throw new SaldoMenorException("No puede sacar mas de " + montoMaximo + " $");
    }
  }
}
