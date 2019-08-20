package pl.jozwik.quillgeneric.cassandra.monix

import org.scalatest.concurrent.ScalaFutures
import pl.jozwik.quillgeneric.cassandra.model.AddressId
import pl.jozwik.quillgeneric.cassandra.monix.repository.AddressMonixRepository

trait AddressSuite extends AbstractMonixMonixSpec with ScalaFutures {

  private lazy val repository = new AddressMonixRepository(ctx)

  "AddressSuite simple transformation" should {

      "run async crud operations" in {
        val id      = AddressId.random
        val address = createAddress(id)
        repository.create(address).runSyncUnsafe()
        repository.create(address).runSyncUnsafe()
        val all = repository.all.runSyncUnsafe()
        logger.debug(s"$all")
        repository.read(id).runSyncUnsafe() shouldBe Option(address)
        repository.all.runSyncUnsafe() shouldBe Seq(address)
        repository.deleteAll.runSyncUnsafe()
        repository.all.runSyncUnsafe() shouldBe Seq.empty
      }

    }

}
