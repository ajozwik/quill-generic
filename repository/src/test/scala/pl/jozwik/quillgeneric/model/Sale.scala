package pl.jozwik.quillgeneric.model

import io.getquill.Embedded
import pl.jozwik.quillgeneric.repository.WithId

import java.time.{ Instant, LocalDate, LocalDateTime }

final case class SaleId(fk1: ProductId, fk2: PersonId) extends Embedded {
  def productId: ProductId = fk1
  def personId: PersonId   = fk2
}

final case class Sale(id: SaleId, saleDate: Instant, saleDateTime: LocalDateTime, createDate: LocalDate) extends WithId[SaleId]
