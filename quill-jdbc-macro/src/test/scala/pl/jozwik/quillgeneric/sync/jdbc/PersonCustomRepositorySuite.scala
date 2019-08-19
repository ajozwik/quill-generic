package pl.jozwik.quillgeneric.sync.jdbc

import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.sync.jdbc.repository.PersonCustomRepository
import org.scalatest.TryValues._
import scala.util.Try

trait PersonCustomRepositorySuite extends AbstractJdbcSpec {
  "PersonCustomRepository " should {
      "Call all operations on Person with auto generated id and custom field" in {
        val repository = new PersonCustomRepository(ctx, "Person3")
        logger.debug("generated id with custom field")
        val person = Person(PersonId.empty, "firstName", "lastName", today)
        repository.all shouldBe Try(Seq())
        val personId         = repository.create(person)
        val personIdProvided = personId.success.value
        val createdPatron    = repository.read(personIdProvided).success.value.getOrElse(fail())
        repository.update(createdPatron) shouldBe 'success
        repository.all shouldBe Try(Seq(createdPatron))
        val newBirthDate = createdPatron.birthDate.minusYears(1)
        val modified     = createdPatron.copy(birthDate = newBirthDate)
        repository.update(modified) shouldBe 'success
        repository.read(createdPatron.id).success.value.map(_.birthDate) shouldBe Option(newBirthDate)

        repository.delete(createdPatron.id) shouldBe 'success
        repository.read(createdPatron.id).success.value shouldBe empty
        repository.all shouldBe Try(Seq())
        repository.deleteAll shouldBe 'success

      }
    }
}
