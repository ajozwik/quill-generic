package pl.jozwik.quillgeneric.zio.repository

import cats.Monad
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{Product, ProductId}
import pl.jozwik.quillgeneric.zio.*
import zio.Task
final class ProductGenRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: ZioJdbcRepository.ZioJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Product"
)(implicit protected val monad: Monad[Task]) extends ZioJdbcRepositoryWithGeneratedId[ProductId, Product, Dialect, Naming] {

  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Product] =
    context.dynamicQuerySchema[Product](tableName)

  private def find(id: ProductId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Task[Seq[Product]] =
    run(dynamicSchema)

  override def create(entity: Product, generateId: Boolean = true): Task[ProductId] =
    if (generateId) {
      run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
    } else {
      run(dynamicSchema.insertValue(entity).returning(_.id))
    }

  override def createOrUpdate(entity: Product, generateId: Boolean = true): Task[ProductId] = {
    inTransaction {
      toTask {
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
  }

  override def read(id: ProductId): Task[Option[Product]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Product): Task[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: ProductId): Task[Long] =
    run(find(id).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
