package pl.jozwik.quillgeneric.model

import java.time.Instant

import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey2, WithId }

final case class SaleId(fk1: ProductId, fk2: PersonId) extends CompositeKey2[ProductId, PersonId] {
  def productId: ProductId = fk1
  def personId: PersonId   = fk2
}

final case class Sale(id: SaleId, saleDate: Instant) extends WithId[SaleId]
