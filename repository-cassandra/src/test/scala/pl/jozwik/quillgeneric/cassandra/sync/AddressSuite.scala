package pl.jozwik.quillgeneric.cassandra.sync

import org.scalatest.TryValues.*
import pl.jozwik.quillgeneric.cassandra.model.AddressId
import pl.jozwik.quillgeneric.cassandra.sync.repository.AddressRepository

trait AddressSuite extends AbstractSyncSpec {

  private lazy val repository = new AddressRepository(ctx)

  "really simple transformation" should {

    "run sync" in {
      val id      = AddressId.random
      val address = createAddress(id)
      repository.create(address).success.value
      val add = repository.createAndRead(address.copy(localNumber = Option("F"))).success.value
      val v = repository.read(id).success.value
      v shouldBe Option(add)
      repository.all.success.value shouldBe Seq(add)
      repository.deleteAll.success.value
      repository.all.success.value shouldBe Seq.empty
    }

  }

}
