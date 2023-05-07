package pl.jozwik.quillgeneric.monad.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class ConfigurationRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String = "Configuration"
)
  extends TryJdbcRepository[ConfigurationId, Configuration, D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Configuration] = {
    import context._
    context.dynamicQuerySchema[Configuration](tableName, alias(_.id, "`KEY`"), alias(_.value, "`VALUE`"))
  }

  override def all: Try[Seq[Configuration]] =
    Try {
      context.all[Configuration](dSchema)
    }

  override def create(entity: Configuration): Try[ConfigurationId] =
    Try {
      context.transaction {
        context.create[ConfigurationId, Configuration](entity)(dSchema)
      }
    }

  override def createOrUpdate(entity: Configuration): Try[ConfigurationId] =
    Try {
      context.transaction {
        context.createOrUpdate[ConfigurationId, Configuration](entity)
      }
    }

  override def read(id: ConfigurationId): Try[Option[Configuration]] =
    Try {
      context.read[ConfigurationId, Configuration](id)(dSchema)
    }

  override def update(entity: Configuration): Try[Long] =
    Try {
      context.transaction {
        context.update[ConfigurationId, Configuration](entity)(dSchema)
      }
    }


  override def delete(id: ConfigurationId): Try[Long] =
    Try {
      context.transaction {
        context.delete[ConfigurationId, Configuration](id)(dSchema)
      }
    }

  override def deleteAll: Try[Long] =
    Try {
      context.transaction {
        context.deleteAll(dSchema)
      }
    }

}
