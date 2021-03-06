package pl.jozwik.quillgeneric.sync.jdbc

import java.time.Instant

import pl.jozwik.quillgeneric.sync.jdbc.repository.AddressRepository
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import org.scalatest.TryValues._

trait AddressSuite extends AbstractJdbcSpec {
  private val repository = new AddressRepository(ctx)

  "AddressSuite " should {
    "Call crud operations " in {
      val id      = AddressId(1)
      val entity  = Address(id, "Country", "City")
      val address = repository.createOrUpdateAndRead(entity).success.value
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
