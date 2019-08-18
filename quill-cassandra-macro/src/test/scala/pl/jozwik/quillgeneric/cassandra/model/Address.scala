package pl.jozwik.quillgeneric.cassandra.model

import java.util.{ Date, UUID }

import pl.jozwik.quillgeneric.quillmacro.WithId

object AddressId {
  val random: AddressId = AddressId(UUID.randomUUID())
}

final case class AddressId(value: UUID) extends AnyVal

final case class Address(
    id: AddressId,
    country: String,
    city: String,
    street: Option[String] = None,
    buildingNumber: Option[String] = None,
    updated: Option[Date] = None,
    localNumber: Option[String] = None
) extends WithId[AddressId]
