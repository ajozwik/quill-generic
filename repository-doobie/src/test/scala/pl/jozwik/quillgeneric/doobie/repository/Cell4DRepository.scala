package pl.jozwik.quillgeneric.doobie.repository

import doobie.ConnectionIO
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.doobie.{ DoobieJdbcContextWithDateQuotes, DoobieRepository }
import pl.jozwik.quillgeneric.model.{ Cell4d, Cell4dId }

final class Cell4DRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: DoobieJdbcContextWithDateQuotes[D, N],
    tableName: String = "Cell4d"
)(implicit protected val monad: cats.Monad[ConnectionIO])
  extends DoobieRepository[Cell4dId, Cell4d, D, N] {

  import context.*

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Cell4d] = {
    context.dynamicQuerySchema[Cell4d](tableName, alias(_.id.fk1, "x"), alias(_.id.fk2, "y"), alias(_.id.fk3, "z"), alias(_.id.fk4, "t"))
  }

  private def find(id: Cell4dId) =
    dynamicSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2)).filter(_.id.fk3 == lift(id.fk3)).filter(_.id.fk4 == lift(id.fk4))

  override def all: ConnectionIO[Seq[Cell4d]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Cell4d): ConnectionIO[Cell4dId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Cell4d): ConnectionIO[Cell4dId] =
    inTransaction {
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

  override def read(id: Cell4dId): ConnectionIO[Option[Cell4d]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Cell4d): ConnectionIO[Long] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: Cell4dId): ConnectionIO[Long] =
    run(find(id).delete)

  override def deleteAll: ConnectionIO[Long] =
    run(dynamicSchema.delete)

}
