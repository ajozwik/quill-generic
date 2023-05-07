package pl.jozwik.quillgeneric.async.jdbc.repository

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.async.AsyncJdbcRepositoryWithGeneratedId
import pl.jozwik.quillgeneric.model.{ Person, PersonId }

import scala.concurrent.{ ExecutionContext, Future }

class PersonAsyncRepository[D <: SqlIdiom, N <: NamingStrategy, C <: ConcreteConnection](
    protected val context: AsyncJdbcContextDateQuotes[D, N, C],
    tableName: String
) extends AsyncJdbcRepositoryWithGeneratedId[PersonId, Person, D, N, C] {

  import context.toFuture

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

  override def createOrUpdate(entity: Person, generatedId: Boolean)(implicit ex: ExecutionContext): Future[PersonId] =
    context.transaction { implicit f =>
      if (generatedId) {
        context.createAndGenerateIdOrUpdate[PersonId, Person](entity)(dSchema, f)
      } else {
        context.createOrUpdate[PersonId, Person](entity)(dSchema, f)
      }
    }

  override def read(id: PersonId)(implicit ex: ExecutionContext): Future[Option[Person]] =
    context.read[PersonId, Person](id)

  override def update(t: Person)(implicit ex: ExecutionContext): Future[Long] =
    context.update[PersonId, Person](t)

  override def delete(id: PersonId)(implicit ex: ExecutionContext): Future[Long] =
    context.delete(id)

  override def deleteAll(implicit ex: ExecutionContext): Future[Long] =
    context.deleteAll

}
