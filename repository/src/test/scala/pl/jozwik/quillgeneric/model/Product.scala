package pl.jozwik.quillgeneric.model

import pl.jozwik.quillgeneric.repository.WithId
import java.time.LocalDate

object ProductId {
  val empty = ProductId(0)
}

final case class ProductId(value: Long) extends AnyVal

final case class Product(id: ProductId, name: String, created: LocalDate = LocalDate.now) extends WithId[ProductId]
