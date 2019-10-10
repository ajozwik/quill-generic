package pl.jozwik.quillgeneric.sync.jdbc

import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.sync.jdbc.repository.PersonRepository

import scala.util.{ Success, Try }
import org.scalatest.TryValues._

trait PersonRepositorySuite extends AbstractJdbcSpec {
  "PersonRepository " should {
    "Call all operations on Person2 with auto generated id" in {
      val repository = new PersonRepository(ctx, "Person2")
      logger.debug("generated id")
      val person = Person(PersonId.empty, "firstName", "lastName", today)
      repository.all shouldBe Try(Seq())
      val personId = repository.createOrUpdate(person)
      personId shouldBe Symbol("success")
      val personIdProvided = personId.success.value
      val createdPatron    = repository.read(personIdProvided).success.value.getOrElse(fail())
      repository.update(createdPatron) shouldBe Symbol("success")
      repository.all shouldBe Success(Seq(createdPatron))
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified     = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified) shouldBe Symbol("success")
      repository.read(createdPatron.id).success.value.map(_.birthDate) shouldBe Option(newBirthDate)
      repository.searchByFirstName(person.firstName)(offset, limit) shouldBe Success(Seq(modified))
      repository.delete(createdPatron.id) shouldBe Symbol("success")
      repository.read(createdPatron.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())
      repository.max shouldBe Success(None)
      repository.deleteAll shouldBe Symbol("success")
    }
  }
}
