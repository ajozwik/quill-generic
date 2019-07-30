package pl.jozwik.quillgeneric.sync

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class PersonRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String)
  extends MyPersonRepository[D, N] {

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
    context.delete[PersonId, Person](id)

}
