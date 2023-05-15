package pl.jozwik.quillgeneric.zio.repository

import cats.Monad
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.Configuration
import pl.jozwik.quillgeneric.model.ConfigurationId
import pl.jozwik.quillgeneric.zio.*
import zio.Task
final class ConfigurationJdbcRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: ZioJdbcRepository.ZioJdbcContextWithDateQuotes[D, N],
    tableName: String = "Configuration"
)(implicit protected val monad: Monad[Task]) extends ZioJdbcRepository[ConfigurationId, Configuration, D, N] {

  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Configuration] = {
    context.dynamicQuerySchema[Configuration](tableName, alias(_.id, "`KEY`"), alias(_.value, "`VALUE`"))
  }

  private def find(id: ConfigurationId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Task[Seq[Configuration]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Configuration): Task[ConfigurationId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Configuration): Task[ConfigurationId] =
    inTransaction {
      toTask {
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
    }

  override def read(id: ConfigurationId): Task[Option[Configuration]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Configuration): Task[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: ConfigurationId): Task[Long] =
    run(find(id).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
