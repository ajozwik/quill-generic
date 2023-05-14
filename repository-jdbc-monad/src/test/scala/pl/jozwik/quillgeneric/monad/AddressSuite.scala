package pl.jozwik.quillgeneric.monad

import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.monad.repository.AddressRepository

import java.time.Instant

trait AddressSuite extends AbstractJdbcSpec {
  private val repository = new AddressRepository(ctx)

  "AddressSuite " should {
    "Call crud operations " in {
      val id        = AddressId(1)
      val entity    = Address(id, "Country", "City")
      val addressId = repository.createOrUpdate(entity).runUnsafe
      val address   = repository.readUnsafe(addressId).runUnsafe
      address shouldBe entity
      val a = repository.searchUpdateAfter(Instant.now).success.value
      a shouldBe empty
      repository.updateAndRead(address.copy(updated = Option(Instant.now)))
      val b = repository.searchUpdateAfter(Instant.EPOCH).success.value
      b should not be empty
      repository.deleteAll shouldBe Symbol("success")
    }
  }
}
