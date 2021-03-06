package pl.jozwik.quillgeneric.cassandra.monix.repository

import io.getquill.NamingStrategy
import monix.eval.Task
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.cassandra.monix.CassandraMonixRepository
import pl.jozwik.quillgeneric.quillmacro.cassandra.monix.CassandraMonixRepository.CassandraMonixContextDateQuotes

final class AddressMonixRepository[Naming <: NamingStrategy](
    protected val context: CassandraMonixContextDateQuotes[Naming],
    protected val tableName: String = "Address"
) extends CassandraMonixRepository[AddressId, Address, Naming] {

  protected def dynamicSchema: context.DynamicEntityQuery[Address] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Address] = {
    import context._
    context.dynamicQuerySchema[Address](tableName, alias(_.city, "city"))
  }

  override def all: Task[Seq[Address]] =
    context.all[Address]

  override def create(entity: Address): Task[AddressId] =
    context.create[AddressId, Address](entity)

  override def createAndRead(entity: Address): Task[Address] =
    context.createAndRead[AddressId, Address](entity)

  override def read(id: AddressId): Task[Option[Address]] =
    context.read[AddressId, Address](id)

  override def readUnsafe(id: AddressId): Task[Address] =
    context.readUnsafe[AddressId, Address](id)

  override def createOrUpdate(entity: Address): Task[AddressId] =
    context.create[AddressId, Address](entity)

  override def createOrUpdateAndRead(entity: Address): Task[Address] =
    context.createAndRead[AddressId, Address](entity)

  override def update(entity: Address): Task[Unit] =
    context.update[AddressId, Address](entity)

  override def updateAndRead(entity: Address): Task[Address] =
    context.updateAndRead[AddressId, Address](entity)

  override def delete(id: AddressId): Task[Unit] =
    context.delete[AddressId, Address](id)

  override def deleteAll: Task[Unit] =
    context.deleteAll

}
