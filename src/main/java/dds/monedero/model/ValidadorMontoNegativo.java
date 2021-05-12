package dds.monedero.model;

import dds.monedero.exceptions.MontoNegativoException;

import java.math.BigDecimal;

public class ValidadorMontoNegativo implements Validador {

  public void validar(BigDecimal monto, BigDecimal montoMaximo) {
    if(monto.signum() == -1) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }
}
