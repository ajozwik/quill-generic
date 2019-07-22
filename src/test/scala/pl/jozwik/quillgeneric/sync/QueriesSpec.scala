package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.Queries

import scala.util.Try

class QueriesSpec extends AbstractSpec {

  lazy val ctx = new H2JdbcContext(SnakeCase, "h2") with Queries
  private lazy val personRepository = new PersonRepository(ctx, "Person")

  private lazy val personRepositoryAutoIncrement = new PersonRepository(ctx, "Person2")

  private lazy val personRepositoryAutoIncrementCustom = new PersonRepository(ctx, "Person3", ((p: Person) => p.birthDate, "dob"))

  private val generateId = true

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  "QueriesSync " should {
    "Call all operations on Person" in {
      val person = Person(PersonId(1), "firstName", "lastName", LocalDate.now)
      val notExisting = Person(PersonId(2), "firstName", "lastName", LocalDate.now)
      personRepository.all shouldBe Try(Seq())
      personRepository.create(person) shouldBe 'success
      personRepository.read(notExisting.id).success.value shouldBe empty
      personRepository.read(person.id).success.value shouldBe Option(person)
      personRepository.update(person) shouldBe 'success
      personRepository.all shouldBe Try(Seq(person))
      personRepository.delete(person.id) shouldBe 'success
      personRepository.all shouldBe Try(Seq())

    }

    "Call all operations on Person2 with auto generated id" in {
      logger.debug("generated id")
      val person = Person(PersonId.empty, "firstName", "lastName", LocalDate.now)
      personRepositoryAutoIncrement.all shouldBe Try(Seq())
      val personId = personRepositoryAutoIncrement.create(person, generateId)
      val personIdProvided = personId.success.value
      val createdPatron = personRepositoryAutoIncrement.read(personIdProvided).success.value.getOrElse(fail())
      personRepositoryAutoIncrement.update(createdPatron) shouldBe 'success
      personRepositoryAutoIncrement.all shouldBe Try(Seq(createdPatron))

      personRepositoryAutoIncrement.delete(createdPatron.id) shouldBe 'success
      personRepositoryAutoIncrement.read(createdPatron.id).success.value shouldBe empty
      personRepositoryAutoIncrement.all shouldBe Try(Seq())
    }

    "Call all operations on Person with auto generated id and custom field" in {
      logger.debug("generated id with custom field")
      val person = Person(PersonId.empty, "firstName", "lastName", LocalDate.now)
      personRepositoryAutoIncrementCustom.all shouldBe Try(Seq())
      val personId = personRepositoryAutoIncrementCustom.create(person, generateId)
      val personIdProvided = personId.success.value
      val createdPatron = personRepositoryAutoIncrementCustom.read(personIdProvided).success.value.getOrElse(fail())
      personRepositoryAutoIncrementCustom.update(createdPatron) shouldBe 'success
      personRepositoryAutoIncrementCustom.all shouldBe Try(Seq(createdPatron))

      personRepositoryAutoIncrementCustom.delete(createdPatron.id) shouldBe 'success
      personRepositoryAutoIncrementCustom.read(createdPatron.id).success.value shouldBe empty
      personRepositoryAutoIncrementCustom.all shouldBe Try(Seq())
    }
  }
}
