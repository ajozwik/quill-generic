package pl.jozwik.quillgeneric.doobie

import pl.jozwik.quillgeneric.doobie.repository.Cell4DRepository
import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }

trait Cell4dSuite extends AbstractDoobieJdbcSpec {

  private val repository = new Cell4DRepository(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {

      repository.all.runUnsafe() shouldBe empty
      val entity = Cell4d(Cell4dId(4, 1, 0, Integer.MAX_VALUE + 1L), false)
      repository.create(entity).runUnsafe() shouldBe entity.id
      repository.createOrUpdate(entity).runUnsafe() shouldBe entity.id
      repository.createOrUpdateAndRead(entity).runUnsafe() shouldBe entity
      repository.all.runUnsafe() should not be empty
      repository.deleteAll.runUnsafe() shouldBe 1
    }
  }
}
