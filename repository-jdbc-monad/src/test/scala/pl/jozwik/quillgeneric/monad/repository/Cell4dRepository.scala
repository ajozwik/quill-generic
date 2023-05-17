package pl.jozwik.quillgeneric.monad.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.*

import scala.util.Try

final class Cell4dRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[Dialect, Naming],
    protected val tableName: String = "Cell4d"
)(implicit protected val monad: Monad[Try])
  extends TryJdbcRepository[Cell4dId, Cell4d, Dialect, Naming] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Cell4d] = {

    context.dynamicQuerySchema[Cell4d](tableName, alias(_.id.fk1, "x"), alias(_.id.fk2, "y"), alias(_.id.fk3, "z"), alias(_.id.fk4, "t"))
  }

  private def find(id: Cell4dId) =
    dynamicSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2)).filter(_.id.fk3 == lift(id.fk3)).filter(_.id.fk4 == lift(id.fk4))

  override def all: Try[Seq[Cell4d]] = Try {
    run(dynamicSchema)
  }

  override def create(entity: Cell4d): Try[Cell4dId] =
    for {
      _ <- Try(run(dynamicSchema.insertValue(entity)))
    } yield {
      entity.id
    }

  override def read(id: Cell4dId): Try[Option[Cell4d]] =
    for {
      seq <- Try(
        run(find(id))
      )
    } yield {
      seq.headOption
    }

  override def createOrUpdate(entity: Cell4d): Try[Cell4dId] =
    inTransaction {
      for {
        el <- Try(
          run(find(entity.id).updateValue(entity))
        )
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

  override def update(entity: Cell4d): Try[Long] =
    Try {
      run(
        find(entity.id).updateValue(entity)
      )
    }

  override def delete(id: Cell4dId): Try[Long] = Try {
    run(
      find(id).delete
    )
  }

  override def deleteAll: Try[Long] =
    Try {
      run(dynamicSchema.delete)
    }

}
