package pl.jozwik.quillgeneric.zio.repository

import cats.Monad
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Sale, SaleId }
import pl.jozwik.quillgeneric.zio.*
import zio.Task
final class SaleJdbcRepositoryGen[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: ZioJdbcRepository.ZioJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Sale"
)(implicit protected val monad: Monad[Task])
  extends ZioJdbcRepository[SaleId, Sale, Dialect, Naming] {

  import context.*

  protected lazy val dynamicSchema: DynamicEntityQuery[Sale] =
    dynamicQuerySchema[Sale](tableName, alias[Sale](_.id.fk1, "PRODUCT_ID"), alias[Sale](_.id.fk2, "PERSON_ID"))
  private def find(id: SaleId): Quoted[EntityQuery[Sale]] =
    dynamicSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))

  override def all: Task[Seq[Sale]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Sale): Task[SaleId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Sale): Task[SaleId] =
    inTransaction {
      toTask {
        for {
          el <- run(find(entity.id).updateValue(lift(entity)))
          id <- el match {
            case 0 =>
              create(entity)
            case _ =>
              pure(entity.id)
          }
        } yield {
          id
        }
      }
    }

  override def read(id: SaleId): Task[Option[Sale]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Sale): Task[Long] =
    run(find(entity.id).updateValue(lift(entity)))

  override def delete(id: SaleId): Task[Long] =
    run(find(id).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
