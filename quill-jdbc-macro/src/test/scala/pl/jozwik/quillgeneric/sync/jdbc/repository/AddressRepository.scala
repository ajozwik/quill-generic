package pl.jozwik.quillgeneric.sync.jdbc.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

import scala.util.Try

final class AddressRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    tableName: String
) extends JdbcRepositoryWithGeneratedId[AddressId, Address, D, N] {

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

  override def createAndRead(entity: Address, generateId: Boolean = true): Try[Address] =
    Try {
      context.transaction {
        if (generateId) {
          context.createWithGenerateIdAndRead[AddressId, Address](entity)
        } else {
          context.createAndRead[AddressId, Address](entity)
        }
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

  override def createOrUpdateAndRead(entity: Address, generateId: Boolean = true): Try[Address] =
    Try {
      context.transaction {
        if (generateId) {
          context.createWithGenerateIdOrUpdateAndRead[AddressId, Address](entity)
        } else {
          context.createOrUpdateAndRead[AddressId, Address](entity)
        }
      }
    }

  override def update(entity: Address): Try[Long] = Try {
    context.update[AddressId, Address](entity)
  }

  override def updateAndRead(entity: Address): Try[Address] = Try {
    context.transaction {
      context.updateAndRead[AddressId, Address](entity)
    }
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
