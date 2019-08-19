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
        repository.create(address)
        repository.create(address)
        val v = repository.read(id).success.get
        v shouldBe Option(address)
        repository.all.success.get shouldBe Seq(address)
        repository.deleteAll
        repository.all.success.get shouldBe Seq.empty
      }

    }

}
