package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;

import java.math.BigDecimal;

public class ValidadorLimiteDepositos implements Validador {

  public void validar(BigDecimal cantidadMovimientos, BigDecimal montoMaximo) {
    if(cantidadMovimientos.intValue() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }
}
