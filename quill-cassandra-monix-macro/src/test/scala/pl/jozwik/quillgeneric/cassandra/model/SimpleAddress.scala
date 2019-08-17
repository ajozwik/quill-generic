package pl.jozwik.quillgeneric.cassandra.model

import java.time.LocalDateTime
import java.util.UUID

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
    //updated: Option[LocalDateTime] = None,
    localNumber: Option[String] = None
) extends WithId[SimpleAddressId]
