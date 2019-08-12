package pl.jozwik.quillgeneric.quillmacro

trait CompositeKey[K1, K2] {
  def fk1: K1
  def fk2: K2
}

trait CompositeKey3[K1, K2, K3] extends CompositeKey[K1, K2] {
  def fk3: K3
}

trait CompositeKey4[K1, K2, K3, K4] extends CompositeKey3[K1, K2, K3] {
  def fk4: K4
}
