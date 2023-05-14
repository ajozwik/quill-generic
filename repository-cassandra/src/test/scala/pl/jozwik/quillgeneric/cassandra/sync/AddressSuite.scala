package pl.jozwik.quillgeneric.cassandra.sync

import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.cassandra.model.AddressId
import pl.jozwik.quillgeneric.cassandra.sync.repository.AddressRepository

trait AddressSuite extends AbstractSyncSpec {

  private lazy val repository = new AddressRepository(ctx)

  "really simple transformation" should {

    "run sync" in {
      val id      = AddressId.random
      val address = createAddress(id)
      repository.create(address).success.value
      repository.create(address).success.value
      val v = repository.read(id).success.value
      v shouldBe Option(address)
      repository.all.success.value shouldBe Seq(address)
      repository.deleteAll.success.value
      repository.all.success.value shouldBe Seq.empty
    }

  }

}
