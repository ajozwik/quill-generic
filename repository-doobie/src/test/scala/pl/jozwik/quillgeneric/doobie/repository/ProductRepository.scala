package pl.jozwik.quillgeneric.doobie.repository

import doobie.*
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.doobie.DoobieRepository.DoobieJdbcContextWithDateQuotes
import pl.jozwik.quillgeneric.doobie.DoobieRepositoryWithTransactionWithGeneratedId
import pl.jozwik.quillgeneric.model.{ Product, ProductId }

final class ProductRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: DoobieJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Product"
)(implicit protected val monad: cats.Monad[ConnectionIO])
  extends DoobieRepositoryWithTransactionWithGeneratedId[ProductId, Product, Dialect, Naming] {

  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Product] =
    context.dynamicQuerySchema[Product](tableName)

  private def find(id: ProductId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: ConnectionIO[Seq[Product]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Product, generateId: Boolean = true): ConnectionIO[ProductId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def createOrUpdate(entity: Product, generateId: Boolean = true): ConnectionIO[ProductId] = {
    inTransaction {
      for {
        el <- run(find(entity.id).updateValue(entity))
        id <- el match {
          case 0 =>
            create(entity, generateId)
          case _ =>
            pure(entity.id)
        }
      } yield {
        id
      }
    }
  }

  override def read(id: ProductId): ConnectionIO[Option[Product]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Product): ConnectionIO[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: ProductId): ConnectionIO[Long] =
    run(find(id).delete)

  override def deleteAll: ConnectionIO[Long] =
    run(dynamicSchema.delete)

}
