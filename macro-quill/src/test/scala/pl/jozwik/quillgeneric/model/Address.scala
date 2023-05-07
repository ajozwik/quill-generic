package pl.jozwik.quillgeneric.model

import pl.jozwik.quillgeneric.repository.WithId

import java.time.Instant

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
    updated: Option[Instant] = None,
    localNumber: Option[String] = None
) extends WithId[AddressId]
