package pl.jozwik.quillgeneric.sync.jdbc

import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }
import pl.jozwik.quillgeneric.sync.jdbc.repository.Cell4dRepository

trait Cell4dSuite extends AbstractJdbcSpec {
  private val repository = new Cell4dRepository(ctx)

  "Cell4dSuite " should {
      "Call crud operations " in {
        val entity = Cell4d(Cell4dId(0, 1, 0, 1), false)
        repository.createOrUpdateAndRead(entity) shouldBe 'success

      }
    }
}
