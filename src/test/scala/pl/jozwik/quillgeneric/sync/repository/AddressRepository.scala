package pl.jozwik.quillgeneric.sync.repository

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.RepositoryWithGeneratedId

import scala.util.Try

final class AddressRepository[D <: SqlIdiom, N <: NamingStrategy](
    protected val context: JdbcContextDateQuotes[D, N],
    tableName: String
)
  extends RepositoryWithGeneratedId[AddressId, Address] {

  private implicit val dSchema: context.DynamicEntityQuery[Address] = context.dynamicQuerySchema[Address](tableName)

  override def all: Try[Seq[Address]] =
    context.all[Address]

  override def create(entity: Address, generateId: Boolean = true): Try[AddressId] =
    if (generateId) {
      context.createAndGenerateId[AddressId, Address](entity)
    } else {
      context.create[AddressId, Address](entity)
    }

  override def createAndRead(entity: Address, generateId: Boolean = true): Try[Address] =
    if (generateId) {
      context.createWithGenerateIdAndRead[AddressId, Address](entity)
    } else {
      context.createAndRead[AddressId, Address](entity)
    }

  override def read(id: AddressId): Try[Option[Address]] =
    context.read[AddressId, Address](id)

  override def createOrUpdate(entity: Address, generateId: Boolean = true): Try[AddressId] =
    if (generateId) {
      context.createAndGenerateIdOrUpdate[AddressId, Address](entity)
    } else {
      context.createOrUpdate[AddressId, Address](entity)
    }

  override def createOrUpdateAndRead(entity: Address, generateId: Boolean = true): Try[Address] =
    if (generateId) {
      context.createWithGenerateIdOrUpdateAndRead[AddressId, Address](entity)
    } else {
      context.createOrUpdateAndRead[AddressId, Address](entity)
    }

  override def update(entity: Address): Try[Long] =
    context.update[Address](entity)

  override def updateAndRead(entity: Address): Try[Address] =
    context.updateAndRead[Address](entity)

  override def delete(id: AddressId): Try[Boolean] =
    context.deleteByFilter[Address](_.id == id)

}
