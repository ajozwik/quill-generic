package pl.jozwik.quillgeneric.model

import pl.jozwik.quillgeneric.quillmacro.WithId

object ProductId {
  val empty = ProductId(0)
}

final case class ProductId(value: Long) extends AnyVal

final case class Product(id: ProductId, name: String) extends WithId[ProductId]
