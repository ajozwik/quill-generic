package pl.jozwik.quillgeneric.monix.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.monix.jdbc.MonixJdbcRepository
import MonixJdbcRepository.MonixJdbcContextDateQuotes
import cats.Monad

class ConfigurationRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: MonixJdbcContextDateQuotes[D, N],
    protected val tableName: String = "Configuration"
)(implicit protected val monad: Monad[Task])
  extends MonixJdbcRepository[ConfigurationId, Configuration, D, N] {
  import context.*

  private val aliases = {
    Seq(
      alias[Configuration](_.id, "`key`"),
      alias[Configuration](_.value, "`value`")
    )
  }
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName, aliases*)

  override def all: Task[Seq[Configuration]] =
    run(dynamicSchema)

  override def create(entity: Configuration): Task[ConfigurationId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Configuration): Task[ConfigurationId] =
    inTransaction {
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

  override def read(id: ConfigurationId): Task[Option[Configuration]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }

  override def update(entity: Configuration): Task[Long] =
    run(dynamicSchema.filter(_.id == lift(entity.id)).updateValue(entity))

  override def delete(id: ConfigurationId): Task[Long] =
    run(dynamicSchema.filter(_.id == lift(id)).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
