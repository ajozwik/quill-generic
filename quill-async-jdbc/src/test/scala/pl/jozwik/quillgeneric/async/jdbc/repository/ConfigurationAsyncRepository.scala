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
) extends AsyncJdbcRepository[ConfigurationId, Configuration, D, N, C] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName)

  override def all(implicit ex: ExecutionContext): Future[Seq[Configuration]] =
    run(dynamicSchema)

  override def create(entity: Configuration)(implicit ex: ExecutionContext): Future[ConfigurationId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Configuration)(implicit ex: ExecutionContext): Future[ConfigurationId] =
    context.transaction { implicit f =>
      for {
        el <- run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity))
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

  override def read(id: ConfigurationId)(implicit ex: ExecutionContext): Future[Option[Configuration]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }
  override def update(entity: Configuration)(implicit ex: ExecutionContext): Future[Long] =
    run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity))

  override def delete(id: ConfigurationId)(implicit ex: ExecutionContext): Future[Long] =
    run(dynamicSchema.filter(_.id == lift(id)).delete)

  override def deleteAll(implicit ex: ExecutionContext): Future[Long] =
    run(dynamicSchema.delete)

}
