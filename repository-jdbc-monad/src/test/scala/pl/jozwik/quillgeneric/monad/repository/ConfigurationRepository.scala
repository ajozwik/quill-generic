package pl.jozwik.quillgeneric.monad.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{Configuration, ConfigurationId}
import pl.jozwik.quillgeneric.monad.TryJdbcRepository
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class ConfigurationRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String = "Configuration"
)(implicit protected val monad: Monad[Try]) extends TryJdbcRepository[ConfigurationId, Configuration, D, N] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Configuration] = {

    context.dynamicQuerySchema[Configuration](tableName, alias(_.id, "`KEY`"), alias(_.value, "`VALUE`"))
  }

  override def all: Try[Seq[Configuration]] =
    Try {
      run(dynamicSchema)
    }

  override def create(entity: Configuration): Try[ConfigurationId] =
    for {
      _ <- Try(run(dynamicSchema.insertValue(entity)))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Configuration): Try[ConfigurationId] =
    inTransaction {
      for {
        el <- Try(run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity)))
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

  override def read(id: ConfigurationId): Try[Option[Configuration]] =
    for {
      seq <- Try(run(dynamicSchema.filter(_.id == lift(id))))
    } yield {
      seq.headOption
    }

  override def update(entity: Configuration): Try[Long] =
    Try {
      context.transaction {
        run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity))
      }
    }

  override def delete(id: ConfigurationId): Try[Long] =
    Try {
      run(dynamicSchema.filter(_.id == lift(id)).delete)
    }


  override def deleteAll: Try[Long] =
    Try {
      run(dynamicSchema.delete)
    }

}
