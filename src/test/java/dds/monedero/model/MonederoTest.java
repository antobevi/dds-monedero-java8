package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner() {
    cuenta.poner(new BigDecimal(1500));
    assertEquals(cuenta.getSaldo(), new BigDecimal(1500));
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(new BigDecimal(-1500)));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(new BigDecimal(1500));
    cuenta.poner(new BigDecimal(456));
    cuenta.poner(new BigDecimal(1900));
    assertEquals(cuenta.getSaldo(), new BigDecimal(3856));
    assertEquals(cuenta.cantidadDepositos(), new BigDecimal(3));
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(new BigDecimal(1500));
          cuenta.poner(new BigDecimal(456));
          cuenta.poner(new BigDecimal(1900));
          cuenta.poner(new BigDecimal(245));
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(new BigDecimal(90));
          cuenta.sacar(new BigDecimal(1001));
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(new BigDecimal(5000));
      cuenta.sacar(new BigDecimal(1001));
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(new BigDecimal(-500)));
  }

  @Test
  public void MontoExtraidoAlDia() {
    cuenta.agregarMovimiento(LocalDate.of(2021,4,29), new BigDecimal(500),false);
    cuenta.agregarMovimiento(LocalDate.of(2021,5,5), new BigDecimal(100),true);
    assertEquals(cuenta.getMontoExtraidoA(LocalDate.of(2021,4,29)), new BigDecimal(500));
  }

  @Test
  public void ModificarSaldo() {
    cuenta.agregarMovimiento(LocalDate.of(2021,5,5), new BigDecimal(500),true);
    cuenta.agregarMovimiento(LocalDate.of(2021,5,10), new BigDecimal(450),true);
    Movimiento nuevoMovimiento = new Movimiento(LocalDate.now(), new BigDecimal(100),true);
    cuenta.modificarSaldo(nuevoMovimiento);
    assertEquals(cuenta.getSaldo(), new BigDecimal(1050));
  }

}