package pl.jozwik.quillgeneric.quillmacro

trait CompositeKey[K1, K2] {
  def fk1: K1

  def fk2: K2
}
