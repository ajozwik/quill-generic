package pl.jozwik.quillgeneric.async.jdbc.repository

import com.github.mauricio.async.db.Connection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepositoryWithGeneratedId

import scala.concurrent.{ ExecutionContext, Future }

class PersonAsyncRepository[D <: SqlIdiom, N <: NamingStrategy, C <: Connection](
    protected val context: AsyncJdbcContextDateQuotes[D, N, C],
    tableName: String
) extends AsyncJdbcRepositoryWithGeneratedId[PersonId, Person, D, N, C] {

  protected def dynamicSchema: context.DynamicEntityQuery[Person] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Person] =
    context.dynamicQuerySchema[Person](tableName)

  override def all(implicit ex: ExecutionContext): Future[Seq[Person]] =
    context.all[Person]

  override def create(entity: Person, generatedId: Boolean)(implicit ex: ExecutionContext): Future[PersonId] =
    context.transaction { implicit f =>
      if (generatedId) {
        context.createAndGenerateId[PersonId, Person](entity)(dSchema, f)
      } else {
        context.create[PersonId, Person](entity)(dSchema, f)
      }
    }

  override def createAndRead(entity: Person, generatedId: Boolean)(implicit ex: ExecutionContext): Future[Person] =
    context.transaction { implicit f =>
      if (generatedId) {
        context.createWithGenerateIdAndRead[PersonId, Person](entity)(dSchema, f)
      } else {
        context.createAndRead[PersonId, Person](entity)(dSchema, f)
      }
    }

  override def createOrUpdate(entity: Person, generatedId: Boolean)(implicit ex: ExecutionContext): Future[PersonId] =
    context.transaction { implicit f =>
      if (generatedId) {
        context.createAndGenerateIdOrUpdate[PersonId, Person](entity)(dSchema, f)
      } else {
        context.createOrUpdate[PersonId, Person](entity)(dSchema, f)
      }
    }

  override def createOrUpdateAndRead(entity: Person, generatedId: Boolean = true)(implicit ex: ExecutionContext): Future[Person] =
    context.transaction { implicit f =>
      if (generatedId) {
        context.createWithGenerateIdOrUpdateAndRead[PersonId, Person](entity)(dSchema, f)
      } else {
        context.createOrUpdateAndRead[PersonId, Person](entity)(dSchema, f)
      }
    }

  override def read(id: PersonId)(implicit ex: ExecutionContext): Future[Option[Person]] =
    context.read[PersonId, Person](id)

  override def readUnsafe(id: PersonId)(implicit ex: ExecutionContext): Future[Person] =
    context.readUnsafe[PersonId, Person](id)

  override def update(t: Person)(implicit ex: ExecutionContext): Future[UP] =
    context.update[PersonId, Person](t)

  override def updateAndRead(entity: Person)(implicit ex: ExecutionContext): Future[Person] =
    context.transaction { f =>
      context.updateAndRead[PersonId, Person](entity)(dSchema, f)
    }

  override def delete(id: PersonId)(implicit ex: ExecutionContext): Future[UP] =
    context.delete(id)

  override def deleteAll(implicit ex: ExecutionContext): Future[UP] =
    context.deleteAll

}
