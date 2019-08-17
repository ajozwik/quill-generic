package pl.jozwik.quillgeneric.cassandra.model

import java.util.{ Date, UUID }

import pl.jozwik.quillgeneric.quillmacro.WithId

object SimpleAddressId {
  val random: SimpleAddressId = SimpleAddressId(UUID.randomUUID())
}

final case class SimpleAddressId(value: UUID) extends AnyVal

final case class SimpleAddress(
    id: SimpleAddressId,
    country: String,
    city: String,
    street: Option[String] = None,
    buildingNumber: Option[String] = None,
    updated: Option[Date] = None,
    localNumber: Option[String] = None
) extends WithId[SimpleAddressId]
