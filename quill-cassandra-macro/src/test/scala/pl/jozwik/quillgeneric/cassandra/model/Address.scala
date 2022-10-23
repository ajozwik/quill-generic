package pl.jozwik.quillgeneric.cassandra.model

import pl.jozwik.quillgeneric.quillmacro.WithId

import java.time.LocalDate
import java.util.{ Date, UUID }

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
    localNumber: Option[String] = None,
    created: Option[LocalDate] = None
) extends WithId[AddressId]
