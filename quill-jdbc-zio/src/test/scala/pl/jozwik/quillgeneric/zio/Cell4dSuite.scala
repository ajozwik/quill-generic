package pl.jozwik.quillgeneric.zio

import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }
import pl.jozwik.quillgeneric.zio.repository.Cell4dJdbcRepository
import zio.interop.catz.*
trait Cell4dSuite extends AbstractZioJdbcSpec {

  private val repository = new Cell4dJdbcRepository(ctx)

  "Cell4dSuite " should {
    "Call crud operations " in {
      repository.all.runUnsafe() shouldBe empty
      val entity = Cell4d(Cell4dId(4, 1, 0, Integer.MAX_VALUE + 1L), false)
      repository.create(entity).runUnsafe() shouldBe entity.id
      repository.createOrUpdate(entity).runUnsafe() shouldBe entity.id
      repository.update(entity).runUnsafe() shouldBe 1
      repository.createOrUpdateAndRead(entity).runUnsafe() shouldBe entity
      repository.deleteAll.runUnsafe() shouldBe 1
    }
  }
}
