package pl.jozwik.quillgeneric.sync.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Product, ProductId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

import scala.util.Try

class ProductRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String = "Product")
  extends JdbcRepositoryWithGeneratedId[ProductId, Product, D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[Product] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Product] =
    context.dynamicQuerySchema[Product](tableName)

  override def create(entity: Product, generateId: Boolean = true): Try[ProductId] =
    if (generateId) {
      context.createAndGenerateId[ProductId, Product](entity)
    } else {
      context.create[ProductId, Product](entity)
    }

  override def createAndRead(entity: Product, generateId: Boolean = true): Try[Product] =
    if (generateId) {
      context.createWithGenerateIdAndRead[ProductId, Product](entity)
    } else {
      context.createAndRead[ProductId, Product](entity)
    }

  override def createOrUpdate(entity: Product, generateId: Boolean = true): Try[ProductId] =
    if (generateId) {
      context.createAndGenerateIdOrUpdate[ProductId, Product](entity)
    } else {
      context.createOrUpdate[ProductId, Product](entity)
    }

  override def createOrUpdateAndRead(entity: Product, generateId: Boolean = true): Try[Product] =
    if (generateId) {
      context.createWithGenerateIdOrUpdateAndRead[ProductId, Product](entity)
    } else {
      context.createOrUpdateAndRead[ProductId, Product](entity)
    }

  override def all: Try[Seq[Product]] =
    context.all[Product]

  override def read(id: ProductId): Try[Option[Product]] =
    context.read[ProductId, Product](id)

  override def update(entity: Product): Try[Long] =
    context.update[ProductId, Product](entity)

  override def updateAndRead(entity: Product): Try[Product] =
    context.updateAndRead[ProductId, Product](entity)(dSchema)

  override def delete(id: ProductId): Try[Boolean] =
    context.delete[ProductId, Product](id)

}
