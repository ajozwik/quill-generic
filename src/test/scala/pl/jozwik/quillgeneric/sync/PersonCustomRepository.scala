package pl.jozwik.quillgeneric.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.{ QuillCrudWithContext, RepositoryWithGeneratedId }

import scala.util.Try

final class PersonCustomRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContext[Dialect, Naming] with QuillCrudWithContext,
    tableName: String)
  extends RepositoryWithGeneratedId[PersonId, Person] {

  private val aliases = {
    import context._
    Seq {
      alias[Person](_.birthDate, "dob")
    }
  }
  private implicit val dSchema: context.DynamicEntityQuery[Person] = context.dynamicQuerySchema[Person](tableName, aliases: _*)

  override def all: Try[Seq[Person]] =
    context.all[Person](dSchema)

  override def create(entity: Person, generateId: Boolean = true): Try[PersonId] =
    if (generateId) {
      context.createAndGenerateId[PersonId, Person](entity)(dSchema)
    } else {
      context.create[PersonId, Person](entity)(dSchema)
    }

  override def read(id: PersonId): Try[Option[Person]] =
    context.read[PersonId, Person](id)(dSchema)

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Try[PersonId] =
    if (generateId) {
      context.createAndGenerateIdOrUpdate[PersonId, Person](entity)(dSchema)
    } else {
      context.createOrUpdate[PersonId, Person](entity)(dSchema)
    }

  override def update(entity: Person): Try[Long] =
    context.update[Person](entity)(dSchema)

  override def delete(id: PersonId): Try[Boolean] =
    context.delete[PersonId, Person](id)

}
