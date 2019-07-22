package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.{ Person, Person2, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.Queries

import scala.util.Try

class QueriesSpec extends AbstractSpec {

  lazy val ctx = new H2JdbcContext(SnakeCase, "h2") with Queries
  private lazy val repository = new PersonRepository(ctx, "Person")

  private lazy val repository2AutoIncrement = new Person2Repository(ctx, "Person2")

  private lazy val repositoryAutoIncrement = new PersonRepository(ctx, "Person2")

  private val generateId = true

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  "QueriesSync " should {
    "Call all operations on Person" in {
      val person = Person(PersonId(1), "firstName", "lastName", LocalDate.now)
      val notExisting = Person(PersonId(2), "firstName", "lastName", LocalDate.now)
      repository.all shouldBe Try(Seq())
      repository.create(person) shouldBe 'success
      repository.read(notExisting.id).success.value shouldBe empty
      repository.read(person.id).success.value shouldBe Option(person)
      repository.update(person) shouldBe 'success
      repository.all shouldBe Try(Seq(person))
      repository.delete(person.id) shouldBe 'success
      repository.all shouldBe Try(Seq())

    }

    "Call all operations on Person2 with auto generated id" in {
      logger.debug("generated id")
      val person = Person2(None, "firstName", "lastName", LocalDate.now)
      repository2AutoIncrement.all shouldBe Try(Seq())
      val personId = repository2AutoIncrement.create(person, generateId)
      val personIdProvided = personId.success.value
      val createdPatron = repository2AutoIncrement.read(personIdProvided).success.value.getOrElse(fail())
      repository2AutoIncrement.update(createdPatron) shouldBe 'success
      repository2AutoIncrement.all shouldBe Try(Seq(createdPatron))

      repository2AutoIncrement.delete(createdPatron.id) shouldBe 'success
      repository2AutoIncrement.read(createdPatron.id).success.value shouldBe empty
      repository2AutoIncrement.all shouldBe Try(Seq())
    }

    "Call all operations on Person2 with auto generated id without option key" in {
      logger.debug("generated id 2")
      val person = Person(PersonId.empty, "firstName", "lastName", LocalDate.now)
      repositoryAutoIncrement.all shouldBe Try(Seq())
      val personId = repositoryAutoIncrement.create(person, generateId)
      val personIdProvided = personId.success.value
      val createdPatron = repositoryAutoIncrement.read(personIdProvided).success.value.getOrElse(fail())
      repositoryAutoIncrement.update(createdPatron) shouldBe 'success
      repositoryAutoIncrement.all shouldBe Try(Seq(createdPatron))

      repositoryAutoIncrement.delete(createdPatron.id) shouldBe 'success
      repositoryAutoIncrement.read(createdPatron.id).success.value shouldBe empty
      repositoryAutoIncrement.all shouldBe Try(Seq())
    }
  }
}
