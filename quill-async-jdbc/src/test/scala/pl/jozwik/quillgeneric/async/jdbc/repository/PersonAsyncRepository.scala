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
  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Person] =
    context.dynamicQuerySchema[Person](tableName)

  override def all(implicit ex: ExecutionContext): Future[Seq[Person]] =
    run(dynamicSchema)

  override def create(entity: Person, generateId: Boolean)(implicit ex: ExecutionContext): Future[PersonId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def createOrUpdate(entity: Person, generateId: Boolean)(implicit ex: ExecutionContext): Future[PersonId] =
    context.transaction { implicit f =>
      for {
        el <- run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity))
        id <- el match {
          case 0 =>
            create(entity, generateId)
          case _ =>
            pure(entity.id)
        }
      } yield {
        id
      }
    }

  override def read(id: PersonId)(implicit ex: ExecutionContext): Future[Option[Person]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }

  override def update(entity: Person)(implicit ex: ExecutionContext): Future[Long] =
    run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity))

  override def delete(id: PersonId)(implicit ex: ExecutionContext): Future[Long] =
    run(dynamicSchema.filter(_.id == lift(id)).delete)

  override def deleteAll(implicit ex: ExecutionContext): Future[Long] =
    run(dynamicSchema.delete)

}
