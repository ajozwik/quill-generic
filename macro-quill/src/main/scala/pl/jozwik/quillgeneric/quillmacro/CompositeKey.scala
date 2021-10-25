package pl.jozwik.quillgeneric.quillmacro

import io.getquill.Embedded

trait CompositeKey[K1, K2] extends Embedded {
  def fk1: K1

  def fk2: K2
}

trait CompositeKey2[K1, K2] extends CompositeKey[K1, K2]

trait CompositeKey3[K1, K2, K3] extends CompositeKey[K1, K2] {
  def fk3: K3
}

trait CompositeKey4[K1, K2, K3, K4] extends CompositeKey[K1, K2] {
  def fk3: K3
  def fk4: K4
}
