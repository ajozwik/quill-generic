package pl.jozwik.quillgeneric.sync.mirror.repository

import io.getquill.NamingStrategy
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.model.{ Configuration, ConfigurationId }
import pl.jozwik.quillgeneric.quillmacro.mirror.{ MirrorContextDateQuotes, MirrorRepository }

class ConfigurationRepository[D <: Idiom, N <: NamingStrategy](
    protected val context: MirrorContextDateQuotes[D, N],
    protected val tableName: String = "Configuration"
) extends MirrorRepository[ConfigurationId, Configuration, D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[Configuration] = dSchema

  private val aliases = {
    import context._
    Seq(
      alias[Configuration](_.id, "key"),
      alias[Configuration](_.value, "value")
    )
  }
  private implicit val dSchema: context.DynamicEntityQuery[Configuration] =
    context.dynamicQuerySchema[Configuration](tableName, aliases: _*)

  override def all: context.QueryMirror[Configuration] =
    context.all[Configuration]

  override def create(entity: Configuration): ConfigurationId =
    context.create[ConfigurationId, Configuration](entity)

  override def createAndRead(entity: Configuration): context.QueryMirror[Configuration] = {
    context.create[ConfigurationId, Configuration](entity)
    read(entity.id)
  }

  override def createOrUpdate(entity: Configuration): ConfigurationId =
    context.createOrUpdate[ConfigurationId, Configuration](entity)

  override def createOrUpdateAndRead(entity: Configuration): context.QueryMirror[Configuration] = {
    context.createOrUpdate[ConfigurationId, Configuration](entity)
    read(entity.id)
  }

  override def read(id: ConfigurationId): context.QueryMirror[Configuration] = {
    import context._
    context.searchByFilter[Configuration](_.id == lift(id))(0, 1)
  }

  override def update(entity: Configuration): context.ActionMirror =
    context.update[ConfigurationId, Configuration](entity)

  override def updateAndRead(entity: Configuration): context.QueryMirror[Configuration] = {
    update(entity)
    read(entity.id)
  }

  override def delete(id: ConfigurationId): context.ActionMirror =
    context.delete[ConfigurationId, Configuration](id)

  override def deleteAll: context.ActionMirror =
    context.deleteAll

}
