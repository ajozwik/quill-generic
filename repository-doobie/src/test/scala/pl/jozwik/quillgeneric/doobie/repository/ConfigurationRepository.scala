package pl.jozwik.quillgeneric.doobie.repository

import cats.Monad
import doobie.ConnectionIO
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.doobie.DoobieRepository
import pl.jozwik.quillgeneric.doobie.DoobieRepository.DoobieJdbcContextWithDateQuotes
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }

final class ConfigurationRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: DoobieJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Configuration"
)(implicit protected val monad: Monad[ConnectionIO])
  extends DoobieRepository[ConfigurationId, Configuration, Dialect, Naming] {

  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Configuration] = {
    context.dynamicQuerySchema[Configuration](tableName, alias(_.id, "`KEY`"), alias(_.value, "`VALUE`"))
  }

  private def find(id: ConfigurationId) = dynamicSchema.filter(_.id == lift(id))

  override def all: ConnectionIO[Seq[Configuration]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Configuration): ConnectionIO[ConfigurationId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Configuration): ConnectionIO[ConfigurationId] =
    inTransaction {
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

  override def read(id: ConfigurationId): ConnectionIO[Option[Configuration]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Configuration): ConnectionIO[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: ConfigurationId): ConnectionIO[Long] =
    run(find(id).delete)

  override def deleteAll: ConnectionIO[Long] =
    run(dynamicSchema.delete)

}
