package dds.monedero.model;

import dds.monedero.exceptions.MaximoExtraccionDiarioException;

import java.math.BigDecimal;

public class ValidadorLimiteExtraccionDiaria implements Validador {

  public void validar(BigDecimal monto, BigDecimal montoMaximo) {
    if(monto.max(montoMaximo) == monto) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + montoMaximo);
    }
  }
}
