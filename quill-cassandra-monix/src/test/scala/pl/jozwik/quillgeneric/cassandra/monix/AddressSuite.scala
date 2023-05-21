package pl.jozwik.quillgeneric.cassandra.monix

import pl.jozwik.quillgeneric.cassandra.model.AddressId
import pl.jozwik.quillgeneric.cassandra.monix.repository.AddressMonixRepository

trait AddressSuite extends AbstractMonixMonixSpec {

  private lazy val repository = new AddressMonixRepository(ctx)

  "AddressSuite simple transformation" should {

    "run async crud operations" in {
      val id      = AddressId.random
      val id2     = AddressId.random
      val address = createAddress(id)
      repository.create(address).runSyncUnsafe()
      val a2 = address.copy(id = id2)
      repository.createOrUpdateAndRead(a2).runSyncUnsafe() shouldBe a2
//        repository.updateAndRead(a2).runSyncUnsafe() shouldBe a2
      repository.createAndRead(a2).runSyncUnsafe() shouldBe a2
      repository.read(id).runSyncUnsafe() shouldBe Option(address)
      intercept[RuntimeException] {
        repository.updateAndRead(a2.copy(id = AddressId.random)).runSyncUnsafe()
      }
      repository.updateAndRead(a2).runSyncUnsafe() shouldBe a2
      repository.update(a2).runSyncUnsafe()
      repository.all.runSyncUnsafe().toSet shouldBe Set(address, a2)
      repository.deleteAll.runSyncUnsafe()

      repository.all.runSyncUnsafe() shouldBe empty

    }

  }

}
