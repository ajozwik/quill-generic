package pl.jozwik.quillgeneric.cassandra.model

import pl.jozwik.quillgeneric.repository.WithId

import java.time.Instant
import java.util.UUID

object AddressId {
  def random: AddressId = AddressId(UUID.randomUUID())
}

final case class AddressId(value: UUID) extends AnyVal

final case class Address(
    id: AddressId,
    country: String,
    city: String,
    street: Option[String] = None,
    buildingNumber: Option[String] = None,
    updated: Option[Instant] = None,
    localNumber: Option[String] = None,
    created: Option[Instant] = None
) extends WithId[AddressId]
