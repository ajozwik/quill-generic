package pl.jozwik.quillgeneric.model

import java.time.LocalDateTime

import pl.jozwik.quillgeneric.quillmacro.WithId

object AddressId {
  val empty: AddressId = AddressId(0)
}

final case class AddressId(value: Int) extends AnyVal

final case class Address(
    id: AddressId,
    country: String,
    city: String,
    street: Option[String] = None,
    buildingNumber: Option[String] = None,
    updated: Option[LocalDateTime] = None,
    localNumber: Option[String] = None
) extends WithId[AddressId]
