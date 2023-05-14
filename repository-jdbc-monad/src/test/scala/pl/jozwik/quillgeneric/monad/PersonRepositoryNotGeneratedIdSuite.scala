package pl.jozwik.quillgeneric.monad

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{Person, PersonId}
import pl.jozwik.quillgeneric.monad.repository.PersonRepository
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes
import java.time.LocalDate
import scala.util.{Success, Try}

object PersonRepositoryNotGeneratedIdSuite {

  def youngerThan[D <: SqlIdiom, N <: NamingStrategy](from: LocalDate, ctx: JdbcContextDateQuotes[D, N]): Try[Seq[Person]] =
    Try {
      import ctx.*
      ctx.run(query[Person].filter(_.birthDate > lift(from)))
    }
}

trait PersonRepositoryNotGeneratedIdSuite extends AbstractJdbcSpec {

  import PersonRepositoryNotGeneratedIdSuite.*

  "PersonRepository not autoincrement key" should {
    "Call all operations on Person" in {
      val repository  = new PersonRepository(ctx, "Person")
      val person      = Person(PersonId(1), "firstName", "lastName", today)
      val notExisting = Person(PersonId(2), "firstName", "lastName", today)
      youngerThan(today, ctx)
      repository.all shouldBe Success(Seq())
      repository.createAndRead(person, !generateId) shouldBe Success(person)
      repository.read(notExisting.id).success.value shouldBe empty
      repository.read(person.id).success.value shouldBe Option(person)
      val personToUpdate = person.copy(lastName = s"${person.lastName}-3")
      repository.updateAndRead(personToUpdate) shouldBe Success(personToUpdate)
      repository.all shouldBe Success(Seq(personToUpdate))
      repository.max shouldBe Success(Option(person.birthDate))
      repository.count shouldBe Success(1)
      repository.delete(personToUpdate.id) shouldBe Symbol("success")
      repository.all shouldBe Success(Seq())
      repository
        .inTransaction {
          for {
            _ <- repository.youngerThan(today)(offset, limit)
            _ <- repository.batchUpdate(Seq(person, notExisting))
          } yield { () }
        }
        .success
        .value
      repository.deleteAll shouldBe Symbol("success")
    }
  }
}
