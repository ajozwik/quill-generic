package pl.jozwik.quillgeneric.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.sync.{ Queries, Repository }

import scala.util.Try

final class AddressRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy](
    protected val context: JdbcContext[Dialect, Naming] with Queries,
    protected val tableName: String
)
  extends Repository[AddressId, Address] {

  override def all: Try[Seq[Address]] =
    context.all[Address]

  override def create(entity: Address, generateId: Boolean = false): Try[AddressId] =
    if (generateId) {
      context.createAndGenerateId[AddressId, Address](entity)
    } else {
      context.create[AddressId, Address](entity)
    }

  override def read(id: AddressId): Try[Option[Address]] =
    context.read[AddressId, Address](id)

  override def createOrUpdate(entity: Address, generateId: Boolean = false): Try[AddressId] =
    context.createOrUpdate[AddressId, Address](entity, generateId)

  override def update(entity: Address): Try[Long] =
    context.update[Address](entity)

  override def update(id: AddressId, action: Address => (Any, Any), actions: Function[Address, (Any, Any)]*): Try[Long] =
    context.updateByFilter[Address](_.id == id, action, actions: _*)

  override def delete(id: AddressId): Try[Boolean] =
    context.deleteByFilter[Address](_.id == id)

}
