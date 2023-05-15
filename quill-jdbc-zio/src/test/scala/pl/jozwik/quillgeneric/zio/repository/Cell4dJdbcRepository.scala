package pl.jozwik.quillgeneric.zio.repository

import cats.Monad
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }
import pl.jozwik.quillgeneric.zio.*
import zio.Task

final class Cell4dJdbcRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: ZioJdbcRepository.ZioJdbcContextWithDateQuotes[D, N],
    tableName: String = "Cell4d"
)(implicit protected val monad: Monad[Task])
  extends ZioJdbcRepository[Cell4dId, Cell4d, D, N] {

  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Cell4d] = {
    context.dynamicQuerySchema[Cell4d](tableName, alias(_.id.fk1, "x"), alias(_.id.fk2, "y"), alias(_.id.fk3, "z"), alias(_.id.fk4, "t"))
  }

  private def find(id: Cell4dId) =
    dynamicSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2)).filter(_.id.fk3 == lift(id.fk3)).filter(_.id.fk4 == lift(id.fk4))

  override def all: Task[Seq[Cell4d]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Cell4d): Task[Cell4dId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Cell4d): Task[Cell4dId] =
    inTransaction {
      toTask {
        for {
          el <- run(find(entity.id).updateValue(entity))
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

  override def read(id: Cell4dId): Task[Option[Cell4d]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Cell4d): Task[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: Cell4dId): Task[Long] =
    run(find(id).delete)

  override def deleteAll: Task[Long] =
    run(dynamicSchema.delete)

}
