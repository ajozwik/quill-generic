package pl.jozwik.quillgeneric.sync.jdbc.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class ConfigurationRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[Dialect, Naming],
    protected val tableName: String = "Configuration"
) extends JdbcRepository[ConfigurationId, Configuration, Dialect, Naming] {

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Configuration] = {
    import context._
    context.dynamicQuerySchema[Configuration](tableName, alias(_.id, "key"))
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

  override def createAndRead(entity: Configuration): Try[Configuration] =
    Try {
      context.transaction {
        context.createAndRead[ConfigurationId, Configuration](entity)
      }
    }

  override def createOrUpdate(entity: Configuration): Try[ConfigurationId] =
    Try {
      context.transaction {
        context.createOrUpdate[ConfigurationId, Configuration](entity)
      }
    }

  override def createOrUpdateAndRead(entity: Configuration): Try[Configuration] =
    Try {
      context.transaction {
        context.createOrUpdateAndRead[ConfigurationId, Configuration](entity)
      }
    }

  override def read(id: ConfigurationId): Try[Option[Configuration]] =
    Try {
      context.read[ConfigurationId, Configuration](id)(dSchema)
    }

  override def readUnsafe(id: ConfigurationId): Try[Configuration] =
    Try {
      context.readUnsafe[ConfigurationId, Configuration](id)(dSchema)
    }

  override def update(entity: Configuration): Try[Long] =
    Try {
      context.transaction {
        context.update[ConfigurationId, Configuration](entity)(dSchema)
      }
    }

  override def updateAndRead(entity: Configuration): Try[Configuration] =
    Try {
      context.transaction {
        context.updateAndRead[ConfigurationId, Configuration](entity)
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
