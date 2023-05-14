package pl.jozwik.quillgeneric.monad.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Product, ProductId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryWithGeneratedId

import scala.util.Try

class ProductRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    protected val tableName: String = "Product"
)(implicit protected val monad: Monad[Try])
  extends TryJdbcRepositoryWithGeneratedId[ProductId, Product, D, N] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Product] =
    context.dynamicQuerySchema[Product](tableName)

  private def find(id: ProductId) =
    dynamicSchema.filter(_.id == lift(id))

  override def create(entity: Product, generateId: Boolean = true): Try[ProductId] =
    Try {
      if (generateId) {
        run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
      } else {
        run(dynamicSchema.insertValue(entity).returning(_.id))
      }
    }

  override def createOrUpdate(entity: Product, generateId: Boolean = true): Try[ProductId] =
    inTransaction {
      for {
        el <- Try(run(find(entity.id).updateValue((entity))))
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

  override def all: Try[Seq[Product]] = Try {
    run(dynamicSchema)
  }

  override def read(id: ProductId): Try[Option[Product]] =
    for {
      seq <- Try(run(dynamicSchema.filter(_.id == lift(id))))
    } yield {
      seq.headOption
    }

  override def update(entity: Product): Try[Long] = Try {
    run(find(entity.id).updateValue(entity))
  }

  override def delete(id: ProductId): Try[Long] =
    Try {
      run(find(id).delete)
    }

  override def deleteAll: Try[Long] =
    Try {
      run(dynamicSchema.delete)
    }

}
