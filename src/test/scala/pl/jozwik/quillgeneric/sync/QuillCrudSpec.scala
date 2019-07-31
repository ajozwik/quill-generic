package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.{ H2JdbcContext, NamingStrategy, SnakeCase }
import org.scalatest.TryValues._
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId, Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.QuillCrudWithContext

import scala.util.{ Success, Try }

object QuillCrudSpec {
  def youngerThan[D <: SqlIdiom, N <: NamingStrategy](
    from: LocalDate,
    ctx: JdbcContextDateQuotes[D, N]): Try[Seq[Person]] =
    Try {
      import ctx._
      ctx.run(query[Person].filter(_.birthDate > lift(from)))
    }
}

class QuillCrudSpec extends AbstractSpec {

  import QuillCrudSpec._

  private lazy val ctx = new H2JdbcContext(SnakeCase, "h2") with QuillCrudWithContext with DateQuotes

  private val generateId = true

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

  "QueriesSync " should {
    "Call all operations on Person" in {
      val repository = new PersonRepository(ctx, "Person")
      val person = Person(PersonId(1), "firstName", "lastName", today)
      val notExisting = Person(PersonId(2), "firstName", "lastName", today)
      youngerThan(today, ctx)
      repository.all shouldBe Success(Seq())
      repository.create(person, !generateId) shouldBe 'success
      repository.read(notExisting.id).success.value shouldBe empty
      repository.read(person.id).success.value shouldBe Option(person)
      repository.update(person) shouldBe 'success
      repository.all shouldBe Success(Seq(person))
      repository.max shouldBe Success(Option(person.birthDate))
      repository.count shouldBe Success(1)
      repository.delete(person.id) shouldBe 'success
      repository.all shouldBe Success(Seq())
      repository.youngerThan(today) shouldBe 'success

    }

    "Call all operations on Person with auto generated id and custom field" in {
      val repository = new PersonCustomRepository(ctx, "Person3")
      logger.debug("generated id with custom field")
      val person = Person(PersonId.empty, "firstName", "lastName", today)
      repository.all shouldBe Try(Seq())
      val personId = repository.create(person)
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

    "Call all operations on Person2 with auto generated id" in {
      val repository = new PersonRepository(ctx, "Person2")
      logger.debug("generated id")
      val person = Person(PersonId.empty, "firstName", "lastName", today)
      repository.all shouldBe Try(Seq())
      val personId = repository.createOrUpdate(person)
      personId shouldBe 'success
      val personIdProvided = personId.success.value
      val createdPatron = repository.read(personIdProvided).success.value.getOrElse(fail())
      repository.update(createdPatron) shouldBe 'success
      repository.all shouldBe Success(Seq(createdPatron))
      val newBirthDate = createdPatron.birthDate.minusYears(1)
      val modified = createdPatron.copy(birthDate = newBirthDate)
      repository.update(modified) shouldBe 'success
      repository.read(createdPatron.id).success.value.map(_.birthDate) shouldBe Option(newBirthDate)
      repository.searchByFirstName(person.firstName) shouldBe Success(Seq(modified))
      repository.delete(createdPatron.id) shouldBe 'success
      repository.read(createdPatron.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())
      repository.max shouldBe Success(None)
    }

    "Handle simple Configuration with custom id" in {
      val repository = new ConfigurationRepository(ctx)
      //      logger.debug("configuration")
      val entity = Configuration(ConfigurationId("firstName"), "lastName")
      val entity2 = Configuration(ConfigurationId("nextName"), "nextName")
      repository.all shouldBe Try(Seq())
      val entityId = repository.createOrUpdate(entity)
      entityId shouldBe 'success
      val entityIdProvided = entityId.success.value
      val createdEntity = repository.read(entityIdProvided).success.value.getOrElse(fail())
      repository.update(createdEntity) shouldBe 'success
      repository.all shouldBe Success(Seq(createdEntity))
      val newValue = "newValue"
      val modified = createdEntity.copy(value = newValue)
      repository.update(modified) shouldBe 'success
      repository.createOrUpdate(modified) shouldBe 'success
      repository.read(createdEntity.id).success.value.map(_.value) shouldBe Option(newValue)
      repository.delete(createdEntity.id) shouldBe 'success
      repository.read(createdEntity.id).success.value shouldBe empty
      repository.all shouldBe Try(Seq())

      repository.createOrUpdate(entity) shouldBe 'success
      repository.createOrUpdate(entity2) shouldBe 'success
      repository.createOrUpdate(entity2) shouldBe 'success
      repository.update(entity2) shouldBe 'success
      repository.all.success.value should contain theSameElementsAs Seq(entity, entity2)

    }
  }
}
