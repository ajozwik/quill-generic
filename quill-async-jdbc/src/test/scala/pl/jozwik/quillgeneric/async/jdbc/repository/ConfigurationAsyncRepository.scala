package pl.jozwik.quillgeneric.async.jdbc.repository

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.async.AsyncJdbcRepository
import pl.jozwik.quillgeneric.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }

import scala.concurrent.{ ExecutionContext, Future }

class ConfigurationAsyncRepository[D <: SqlIdiom, N <: NamingStrategy, C <: ConcreteConnection](
    protected val context: AsyncJdbcContextDateQuotes[D, N, C],
    tableName: String
)(implicit protected val ec: ExecutionContext) extends AsyncJdbcRepository[ConfigurationId, Configuration, D, N, C] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName)

  private def find(id: ConfigurationId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Future[Seq[Configuration]] =
    run(dynamicSchema)

  override def create(entity: Configuration): Future[ConfigurationId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Configuration): Future[ConfigurationId] =
    context.transaction { implicit f =>
      for {
        el <- run(find(entity.id).updateValue(entity))
        id <- el match {
          case 0 =>
            create(entity)
          case _ =>
            pure(entity.id)
        }
      } yield {
        id
      }
    }

  override def read(id: ConfigurationId): Future[Option[Configuration]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }
  override def update(entity: Configuration): Future[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: ConfigurationId): Future[Long] =
    run(find(id).delete)

  override def deleteAll: Future[Long] =
    run(dynamicSchema.delete)

}
