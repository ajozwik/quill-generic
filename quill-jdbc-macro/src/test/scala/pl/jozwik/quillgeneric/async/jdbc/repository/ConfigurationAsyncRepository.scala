package pl.jozwik.quillgeneric.async.jdbc.repository

import com.github.mauricio.async.db.Connection
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepository
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes

import scala.concurrent.{ ExecutionContext, Future }

class ConfigurationAsyncRepository[D <: SqlIdiom, N <: NamingStrategy, C <: Connection](
    protected val context: AsyncJdbcContextDateQuotes[D, N, C],
    tableName: String
) extends AsyncJdbcRepository[ConfigurationId, Configuration, D, N, C] {

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName)

  override def all(implicit ex: ExecutionContext): Future[Seq[Configuration]] =
    context.all[Configuration]

  override def create(entity: Configuration)(implicit ex: ExecutionContext): Future[ConfigurationId] =
    context.create[ConfigurationId, Configuration](entity)

  override def createAndRead(entity: Configuration)(implicit ex: ExecutionContext): Future[Configuration] =
    context.transaction { f =>
      context.createAndRead[ConfigurationId, Configuration](entity)(dSchema, f)
    }

  override def createOrUpdate(entity: Configuration)(implicit ex: ExecutionContext): Future[ConfigurationId] =
    context.transaction { f =>
      context.createOrUpdate[ConfigurationId, Configuration](entity)(dSchema, f)
    }

  override def createOrUpdateAndRead(entity: Configuration)(implicit ex: ExecutionContext): Future[Configuration] =
    context.transaction { f =>
      context.createOrUpdateAndRead[ConfigurationId, Configuration](entity)(dSchema, f)
    }

  override def read(id: ConfigurationId)(implicit ex: ExecutionContext): Future[Option[Configuration]] =
    context.read[ConfigurationId, Configuration](id)

  override def update(t: Configuration)(implicit ex: ExecutionContext): Future[U] =
    context.update[ConfigurationId, Configuration](t)

  override def updateAndRead(entity: Configuration)(implicit ex: ExecutionContext): Future[Configuration] =
    context.transaction { f =>
      context.updateAndRead[ConfigurationId, Configuration](entity)(dSchema, f)
    }

  override def delete(id: ConfigurationId)(implicit ex: ExecutionContext): Future[U] =
    context.delete(id)

  override def deleteAll(implicit ex: ExecutionContext): Future[U] =
    context.deleteAll

}
