package pl.jozwik.quillgeneric.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.QuillCrudWithContext

import scala.util.Try

final class PersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContext[Dialect, Naming] with QuillCrudWithContext,
    protected val tableName: String)
  extends MyPersonRepository[Dialect, Naming] {

  protected def dynamicSchema: context.DynamicEntityQuery[Person] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Person] =
    context.dynamicQuerySchema[Person](tableName)

  override def all: Try[Seq[Person]] =
    context.all[Person](dSchema)

  override def create(entity: Person, generateId: Boolean = true): Try[PersonId] =
    if (generateId) {
      context.createAndGenerateId[PersonId, Person](entity)(dSchema)
    } else {
      context.create[PersonId, Person](entity)(dSchema)
    }

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Try[PersonId] =
    if (generateId) {
      context.createAndGenerateIdOrUpdate[PersonId, Person](entity)(dSchema)
    } else {
      context.createOrUpdate[PersonId, Person](entity)(dSchema)
    }

  override def read(id: PersonId): Try[Option[Person]] =
    context.read[PersonId, Person](id)(dSchema)

  override def update(entity: Person): Try[Long] =
    context.update[Person](entity)(dSchema)

  override def delete(id: PersonId): Try[Boolean] =
    context.deleteByFilter[Person](_.id == id)(dSchema)

}
