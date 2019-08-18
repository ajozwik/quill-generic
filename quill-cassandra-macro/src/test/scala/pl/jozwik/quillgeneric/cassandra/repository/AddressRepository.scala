package pl.jozwik.quillgeneric.cassandra.repository

import io.getquill.NamingStrategy
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.cassandra.sync.CassandraRepository
import pl.jozwik.quillgeneric.quillmacro.cassandra.sync.CassandraRepository.CassandraContextDateQuotes

import scala.util.Try

final class AddressRepository[Naming <: NamingStrategy](
    protected val context: CassandraContextDateQuotes[Naming],
    protected val tableName: String = "Address"
) extends CassandraRepository[AddressId, Address, Naming] {

  protected def dynamicSchema: context.DynamicEntityQuery[Address] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Address] = {
    import context._
    context.dynamicQuerySchema[Address](tableName, alias(_.city, "city"))
  }

  override def all: Try[Seq[Address]] = Try {
    context.all[Address]
  }

  override def create(entity: Address): Try[AddressId] = Try {
    context.create[AddressId, Address](entity)
  }

  override def createAndRead(entity: Address): Try[Address] = Try {
    context.createAndRead[AddressId, Address](entity)
  }

  override def read(id: AddressId): Try[Option[Address]] = Try {
    context.read[AddressId, Address](id)
  }

  override def createOrUpdate(entity: Address): Try[AddressId] = Try {
    context.create[AddressId, Address](entity)
  }

  override def createOrUpdateAndRead(entity: Address): Try[Address] = Try {
    context.createAndRead[AddressId, Address](entity)
  }

  override def update(entity: Address): Try[Unit] = Try {
    context.update[AddressId, Address](entity)
  }

  override def updateAndRead(entity: Address): Try[Address] = Try {
    context.updateAndRead[AddressId, Address](entity)
  }

  override def delete(id: AddressId): Try[Unit] = Try {
    context.delete[AddressId, Address](id)
  }

}
