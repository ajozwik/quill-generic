package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.{ H2JdbcContext, SnakeCase }
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.{ Person, PersonId }

import scala.util.Success

class QueriesSpec extends AbstractSpec {

  private lazy val ctx = new H2JdbcContext(SnakeCase, "h2") with Queries
  private lazy val repository = new PersonRepository(ctx)

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  "QueriesAsync " should {
    "Call all operations " in {
      val person = Person(PersonId(1), "firstName", "lastName", LocalDate.now)
      repository.all shouldBe Success(Seq())
      repository.create(person)
      repository.all shouldBe Success(Seq(person))
    }
  }
}
