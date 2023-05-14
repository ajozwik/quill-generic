package pl.jozwik.quillgeneric.monad.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Sale, SaleId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class SaleRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    tableName: String
)(implicit protected val monad: Monad[Try])
  extends AbstractSaleRepository[D, N] {

  import context.*
  private val aliases = {

    Seq(
      alias[Sale](_.id.fk1, "PRODUCT_ID"),
      alias[Sale](_.id.fk2, "PERSON_ID")
    )
  }

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Sale] = context.dynamicQuerySchema[Sale](tableName, aliases: _*)

  override def create(entity: Sale): Try[SaleId] =
    for {
      _ <- Try(run(dynamicSchema.insertValue(entity)))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Sale): Try[SaleId] =
    inTransaction {
      val id = entity.id
      for {
        el <- Try(run(find(id).updateValue(entity)))
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

  private def find(id: SaleId) =
    dynamicSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))

  override def all: Try[Seq[Sale]] =
    Try {
      run(dynamicSchema)
    }

  override def read(id: SaleId): Try[Option[Sale]] = {
    for {
      seq <- Try(run {
        find(id)
      })
    } yield {
      seq.headOption
    }
  }

  override def update(entity: Sale): Try[Long] = Try {
    val id = entity.id
    run(find(id).updateValue(entity))
  }

  override def delete(id: SaleId): Try[Long] =
    Try {
      run(find(id).delete)
    }

  override def deleteAll: Try[Long] =
    Try {
      run(dynamicSchema.delete)
    }
}
