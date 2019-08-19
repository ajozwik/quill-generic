package pl.jozwik.quillgeneric.model

import io.getquill.Embedded
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey4, WithId }

final case class Cell4dId(fk1: Int, fk2: Int, fk3: Int, fk4: Int) extends Embedded with CompositeKey4[Int, Int, Int, Int] {
  def x: Int = fk1

  def y: Int = fk2

  def z: Int = fk3

  def t: Int = fk4

}

final case class Cell4d(id: Cell4dId, occupied: Boolean) extends WithId[Cell4dId]
