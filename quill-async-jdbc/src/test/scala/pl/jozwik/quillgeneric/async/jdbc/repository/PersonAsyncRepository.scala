package pl.jozwik.quillgeneric.async.jdbc.repository

import cats.Monad
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
)(implicit protected val ec: ExecutionContext, protected val monad: Monad[Future])
  extends AsyncJdbcRepositoryWithGeneratedId[PersonId, Person, D, N, C] {
  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Person] =
    context.dynamicQuerySchema[Person](tableName)

  override def all: Future[Seq[Person]] =
    run(dynamicSchema)

  private def find(id: PersonId) =
    dynamicSchema.filter(_.id == lift(id))

  override def create(entity: Person, generateId: Boolean): Future[PersonId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def createOrUpdate(entity: Person, generateId: Boolean): Future[PersonId] =
    inTransaction { implicit f =>
      for {
        el <- run(find(entity.id).updateValue(entity))
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

  override def read(id: PersonId): Future[Option[Person]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }

  override def update(entity: Person): Future[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: PersonId): Future[Long] =
    run(find(id).delete)

  override def deleteAll: Future[Long] =
    run(dynamicSchema.delete)

}
