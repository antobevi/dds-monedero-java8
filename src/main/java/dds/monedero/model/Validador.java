package dds.monedero.model;

import java.math.BigDecimal;

public interface Validador {
  void validar(BigDecimal monto, BigDecimal montoMaximo);

}
