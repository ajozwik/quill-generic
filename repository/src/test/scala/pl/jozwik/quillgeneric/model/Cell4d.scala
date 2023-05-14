package pl.jozwik.quillgeneric.model

import pl.jozwik.quillgeneric.repository.WithId

final case class Cell4dId(fk1: Int, fk2: Int, fk3: Int, fk4: Int) {
  def x: Int = fk1

  def y: Int = fk2

  def z: Int = fk3

  def t: Int = fk4

}

final case class Cell4d(id: Cell4dId, occupied: Boolean) extends WithId[Cell4dId]
