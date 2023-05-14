package pl.jozwik.quillgeneric.monad.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class AddressRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    tableName: String = "Address"
)(implicit protected val monad: Monad[Try])
  extends AbstractAddressRepository[D, N] {
  import context.*
  protected val dynamicSchema: context.DynamicEntityQuery[Address]  = context.dynamicQuerySchema[Address](tableName)

  private def find(id: AddressId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Try[Seq[Address]] =
    Try {
      run(dynamicSchema)
    }

  override def create(entity: Address, generateId: Boolean = true): Try[AddressId] =
    Try {
      if (generateId) {
        run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
      } else {
        run(dynamicSchema.insertValue(entity).returning(_.id))
      }
    }

  override def read(id: AddressId): Try[Option[Address]] =
    for {
      seq <- Try(run(dynamicSchema.filter(_.id == lift(id))))
    } yield {
      seq.headOption
    }

  override def createOrUpdate(entity: Address, generateId: Boolean = true): Try[AddressId] =
    inTransaction {
      for {
        el <- Try(run(find(entity.id).updateValue(entity)))
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

  override def update(entity: Address): Try[Long] =
    Try {
      run(find(entity.id).updateValue(entity))
    }

  override def delete(id: AddressId): Try[Long] =
    Try {
      run(find(id).delete)
    }

  override def deleteAll: Try[Long] =
    Try {
      run(dynamicSchema.delete)
    }

}
