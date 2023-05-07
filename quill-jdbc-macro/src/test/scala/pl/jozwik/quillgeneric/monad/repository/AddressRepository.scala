package pl.jozwik.quillgeneric.monad.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes

import scala.util.Try

final class AddressRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    tableName: String = "Address"
) extends AbstractAddressRepository[D, N] {

  protected implicit val dynamicSchema: context.DynamicEntityQuery[Address] = context.dynamicQuerySchema[Address](tableName)

  override def all: Try[Seq[Address]] =
    Try {
      context.all[Address]
    }

  override def create(entity: Address, generateId: Boolean = true): Try[AddressId] =
    Try {
      if (generateId) {
        context.createAndGenerateId[AddressId, Address](entity)
      } else {
        context.create[AddressId, Address](entity)
      }
    }

  override def read(id: AddressId): Try[Option[Address]] =
    Try {
      context.read[AddressId, Address](id)
    }

  override def createOrUpdate(entity: Address, generateId: Boolean = true): Try[AddressId] =
    Try {
      context.transaction {
        if (generateId) {
          context.createAndGenerateIdOrUpdate[AddressId, Address](entity)
        } else {
          context.createOrUpdate[AddressId, Address](entity)
        }
      }
    }

  override def update(entity: Address): Try[Long] =
    Try {
      context.update[AddressId, Address](entity)
    }

  override def delete(id: AddressId): Try[Long] =
    Try {
      context.delete[AddressId, Address](id)
    }

  override def deleteAll: Try[Long] =
    Try {
      context.deleteAll
    }

}
