package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.{ H2JdbcContext, SnakeCase }
import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.QuillCrudWithContext

import scala.util.{ Success, Try }

class QuillCrudSpec extends AbstractSpec {

  lazy val ctx = new H2JdbcContext(SnakeCase, "h2") with QuillCrudWithContext

  private val generateId = true

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  "QueriesSync " should {
    "Call all operations on Person" in {
      val repository = new PersonRepository(ctx, "Person")
      val person = Person(PersonId(1), "firstName", "lastName", LocalDate.now)
      val notExisting = Person(PersonId(2), "firstName", "lastName", LocalDate.now)
      repository.all shouldBe Success(Seq())
      repository.create(person) shouldBe 'success
      repository.read(notExisting.id).success.value shouldBe empty
      repository.read(person.id).success.value shouldBe Option(person)
      repository.update(person) shouldBe 'success
      repository.all shouldBe Success(Seq(person))
      repository.max shouldBe Success(Option(person.birthDate))
      repository.delete(person.id) shouldBe 'success
      repository.all shouldBe Success(Seq())

    }

    "Call all operations on Person2 with auto generated id" in {
      val repository = new PersonRepository(ctx, "Person2")
      logger.debug("generated id")
      val person = Person(PersonId.empty, "firstName", "lastName", LocalDate.now)
      repository.all shouldBe Try(Seq())
      val personId = repository.create(person, generateId)
      val personIdProvided = personId.success.value
      val createdPatron = repository.read(personIdProvided).success.value.getOrElse(fail())
      repository.update(createdPatron) shouldBe 'success
      repository.all shouldBe Success(Seq(createdPatron))
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified) shouldBe 'success
      repository.read(createdPatron.id).success.value.map(_.birthDate) shouldBe Option(newBirthDate)

      repository.delete(createdPatron.id) shouldBe 'success
      repository.read(createdPatron.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())
      repository.max shouldBe Success(None)
    }

    "Call all operations on Person with auto generated id and custom field" in {
      val repository = new PersonCustomRepository(ctx, "Person3")
      logger.debug("generated id with custom field")
      val person = Person(PersonId.empty, "firstName", "lastName", LocalDate.now)
      repository.all shouldBe Try(Seq())
      val personId = repository.create(person, generateId)
      val personIdProvided = personId.success.value
      val createdPatron = repository.read(personIdProvided).success.value.getOrElse(fail())
      repository.update(createdPatron) shouldBe 'success
      repository.all shouldBe Try(Seq(createdPatron))
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified) shouldBe 'success
      repository.read(createdPatron.id).success.value.map(_.birthDate) shouldBe Option(newBirthDate)

      repository.delete(createdPatron.id) shouldBe 'success
      repository.read(createdPatron.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())

    }
  }
}
