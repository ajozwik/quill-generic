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

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  import context.toFuture

  private implicit val dSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName)

  override def all(implicit ex: ExecutionContext): Future[Seq[Configuration]] =
    context.all[Configuration]

  override def create(entity: Configuration)(implicit ex: ExecutionContext): Future[ConfigurationId] =
    context.create[ConfigurationId, Configuration](entity)

  override def createOrUpdate(entity: Configuration)(implicit ex: ExecutionContext): Future[ConfigurationId] =
    context.transaction { f =>
      context.createOrUpdate[ConfigurationId, Configuration](entity)(dSchema, f)
    }

  override def read(id: ConfigurationId)(implicit ex: ExecutionContext): Future[Option[Configuration]] =
    context.read[ConfigurationId, Configuration](id)

  override def update(t: Configuration)(implicit ex: ExecutionContext): Future[Long] =
    context.update[ConfigurationId, Configuration](t)

  override def delete(id: ConfigurationId)(implicit ex: ExecutionContext): Future[Long] =
    context.delete(id)

  override def deleteAll(implicit ex: ExecutionContext): Future[Long] =
    context.deleteAll

}
