package pl.jozwik.quillgeneric.doobie

import pl.jozwik.quillgeneric.doobie.repository.PersonRepository
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
trait PersonRepositorySuite extends AbstractDoobieJdbcSpec {
  "PersonCustomRepository " should {
    "Call all operations on Person with auto generated id and custom field" in {
      val repository = new PersonRepository(ctx, "Person3")
      logger.debug("generated id with custom field")
      val person = Person(PersonId.empty, "firstName", "lastName", today)
      repository.all.runUnsafe() shouldBe empty
      val personId = repository.create(person).runUnsafe()
      repository.all.runUnsafe() should not be empty
      val createdPatron = repository.read(personId).runUnsafe() match {
        case Some(p) =>
          p
        case _ =>
          fail()
      }
      val task = repository.inTransaction {
        for {
          u   <- repository.update(createdPatron)
          all <- repository.all
        } yield {
          (u, all)
        }
      }
      task.runUnsafe() shouldBe ((1, Seq(createdPatron)))
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified     = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified).runUnsafe() shouldBe 1
      repository.createOrUpdate(modified).runUnsafe() shouldBe modified.id
      repository.createOrUpdateAndRead(modified).runUnsafe() shouldBe modified
      repository.read(createdPatron.id).runUnsafe().map(_.birthDate) shouldBe Option(newBirthDate)

      repository.delete(createdPatron.id).runUnsafe() shouldBe 1
      repository.read(createdPatron.id).runUnsafe() shouldBe empty
      repository.all.runUnsafe() shouldBe empty

      repository.deleteAll.runUnsafe() shouldBe 0

    }
  }
}
