package pl.jozwik.quillgeneric.monad.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Product, ProductId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryWithGeneratedId

import scala.util.Try

class ProductRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String = "Product"
) extends TryJdbcRepositoryWithGeneratedId[ProductId, Product, D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[Product] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Product] =
    context.dynamicQuerySchema[Product](tableName)

  override def create(entity: Product, generateId: Boolean = true): Try[ProductId] = Try {
    if (generateId) {
      context.createAndGenerateId[ProductId, Product](entity)
    } else {
      context.create[ProductId, Product](entity)
    }
  }

  override def createOrUpdate(entity: Product, generateId: Boolean = true): Try[ProductId] =
    Try {
      context.transaction {
        if (generateId) {
          context.createAndGenerateIdOrUpdate[ProductId, Product](entity)
        } else {
          context.createOrUpdate[ProductId, Product](entity)
        }
      }
    }

  override def all: Try[Seq[Product]] = Try {
    context.all[Product]
  }

  override def read(id: ProductId): Try[Option[Product]] = Try {
    context.read[ProductId, Product](id)
  }

  override def update(entity: Product): Try[Long] = Try {
    context.update[ProductId, Product](entity)
  }

  override def delete(id: ProductId): Try[Long] =
    Try {
      context.delete[ProductId, Product](id)
    }

  override def deleteAll: Try[Long] =
    Try {
      context.deleteAll
    }

}
