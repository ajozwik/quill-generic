package pl.jozwik.quillgeneric.monix.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepository
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepository.MonixJdbcContextDateQuotes

class ConfigurationRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: MonixJdbcContextDateQuotes[D, N],
    protected val tableName: String = "Configuration"
) extends MonixJdbcRepository[ConfigurationId, Configuration, D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  private val aliases = {
    import context._
    Seq(
      alias[Configuration](_.id, "`key`"),
      alias[Configuration](_.value, "`value`")
    )
  }
  private implicit val dSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName, aliases: _*)

  override def all: Task[Seq[Configuration]] =
    context.all[Configuration]

  override def create(entity: Configuration): Task[ConfigurationId] =
    context.create[ConfigurationId, Configuration](entity)

  override def createAndRead(entity: Configuration): Task[Configuration] =
    context.transaction {
      context.createAndRead[ConfigurationId, Configuration](entity)
    }

  override def createOrUpdate(entity: Configuration): Task[ConfigurationId] =
    context.transaction {
      context.createOrUpdate[ConfigurationId, Configuration](entity)
    }

  override def createOrUpdateAndRead(entity: Configuration): Task[Configuration] =
    context.transaction {
      context.createOrUpdateAndRead[ConfigurationId, Configuration](entity)
    }

  override def read(id: ConfigurationId): Task[Option[Configuration]] =
    context.read[ConfigurationId, Configuration](id)

  override def readUnsafe(id: ConfigurationId): Task[Configuration] =
    context.readUnsafe[ConfigurationId, Configuration](id)

  override def update(entity: Configuration): Task[Long] =
    context.update[ConfigurationId, Configuration](entity)

  override def updateAndRead(entity: Configuration): Task[Configuration] =
    context.transaction {
      context.updateAndRead[ConfigurationId, Configuration](entity)
    }

  override def delete(id: ConfigurationId): Task[Long] =
    context.delete[ConfigurationId, Configuration](id)

  override def deleteAll: Task[Long] =
    context.deleteAll

}
