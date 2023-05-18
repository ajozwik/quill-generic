package pl.jozwik.quillgeneric.doobie.repository

import doobie.ConnectionIO
import io.getquill.*
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.doobie.{ DoobieJdbcContextWithDateQuotes, DoobieRepository }
import pl.jozwik.quillgeneric.model.{ Sale, SaleId }

final class SaleRepositoryGen[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: DoobieJdbcContextWithDateQuotes[Dialect, Naming],
    tableName: String = "Sale"
)(implicit protected val monad: cats.Monad[ConnectionIO])
  extends DoobieRepository[SaleId, Sale, Dialect, Naming] {

  import context.*

  protected lazy val dynamicSchema: DynamicEntityQuery[Sale] =
    dynamicQuerySchema[Sale](tableName, alias[Sale](_.id.fk1, "PRODUCT_ID"), alias[Sale](_.id.fk2, "PERSON_ID"))

  private def find(id: SaleId): Quoted[EntityQuery[Sale]] =
    dynamicSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))

  override def all: ConnectionIO[Seq[Sale]] =
    for {
      all <- run(dynamicSchema)
    } yield {
      all
    }

  override def create(entity: Sale): ConnectionIO[SaleId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Sale): ConnectionIO[SaleId] =
    inTransaction {
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

  override def read(id: SaleId): ConnectionIO[Option[Sale]] =
    for {
      seq <- run(find(id))
    } yield {
      seq.headOption
    }

  override def update(entity: Sale): ConnectionIO[Long] =
    run(find(entity.id).updateValue(lift(entity)))

  override def delete(id: SaleId): ConnectionIO[Long] =
    run(find(id).delete)

  override def deleteAll: ConnectionIO[Long] =
    run(dynamicSchema.delete)

}
