package pl.jozwik.quillgeneric.monix.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepository.MonixJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepositoryWithGeneratedId

final class PersonCustomRepositoryJdbc[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: MonixJdbcContextDateQuotes[D, N],
    tableName: String
) extends MonixJdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {

  private val aliases = {
    import context._
    Seq {
      alias[Person](_.birthDate, "dob")
    }
  }

  protected def dynamicSchema: context.DynamicEntityQuery[Person] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Person] = context.dynamicQuerySchema[Person](tableName, aliases: _*)

  override def all: Task[Seq[Person]] =
    context.all[Person]

  override def create(entity: Person, generateId: Boolean = true): Task[PersonId] =
    if (generateId) {
      context.createAndGenerateId[PersonId, Person](entity)
    } else {
      context.create[PersonId, Person](entity)
    }

  override def createAndRead(entity: Person, generateId: Boolean = true): Task[Person] =
    context.transaction {
      if (generateId) {
        context.createWithGenerateIdAndRead[PersonId, Person](entity)
      } else {
        context.createAndRead[PersonId, Person](entity)
      }
    }

  override def read(id: PersonId): Task[Option[Person]] =
    context.read[PersonId, Person](id)

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Task[PersonId] =
    context.transaction {
      if (generateId) {
        context.createAndGenerateIdOrUpdate[PersonId, Person](entity)
      } else {
        context.createOrUpdate[PersonId, Person](entity)
      }
    }

  override def createOrUpdateAndRead(entity: Person, generateId: Boolean = true): Task[Person] =
    context.transaction {
      if (generateId) {
        context.createWithGenerateIdOrUpdateAndRead[PersonId, Person](entity)
      } else {
        context.createOrUpdateAndRead[PersonId, Person](entity)
      }
    }

  override def update(entity: Person): Task[Long] =
    context.update[PersonId, Person](entity)

  override def updateAndRead(entity: Person): Task[Person] =
    context.transaction {
      context.updateAndRead[PersonId, Person](entity)
    }

  override def delete(id: PersonId): Task[Long] =
    context.delete[PersonId, Person](id)

  override def deleteAll: Task[Long] =
    context.deleteAll

}
