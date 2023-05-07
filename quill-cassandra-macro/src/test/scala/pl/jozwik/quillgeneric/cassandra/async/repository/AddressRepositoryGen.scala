package pl.jozwik.quillgeneric.cassandra.async.repository

import io.getquill.NamingStrategy
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.cassandra.async.CassandraAsyncRepository
import pl.jozwik.quillgeneric.cassandra.async.CassandraAsyncRepository.CassandraAsyncContextDateQuotes

import scala.concurrent.{ ExecutionContext, Future }

final class AddressRepositoryGen[Naming <: NamingStrategy](
    protected val context: CassandraAsyncContextDateQuotes[Naming],
    protected val tableName: String = "Address"
) extends CassandraAsyncRepository[AddressId, Address, Naming] {

  protected def dynamicSchema: context.DynamicEntityQuery[Address] = dSchema

  private implicit val dSchema: context.DynamicEntityQuery[Address] = {

    context.dynamicQuerySchema[Address](tableName)
  }

  override def all(implicit ec: ExecutionContext): Future[Seq[Address]] =
    context.all[Address](dSchema, ec)

  override def create(entity: Address)(implicit ec: ExecutionContext): Future[AddressId] =
    context.create[AddressId, Address](entity)(dSchema, ec)



  override def createOrUpdate(entity: Address)(implicit ec: ExecutionContext): Future[AddressId] =
    context.create[AddressId, Address](entity)


  override def read(id: AddressId)(implicit ec: ExecutionContext): Future[Option[Address]] =
    context.read[AddressId, Address](id)(dSchema, ec)

  override def update(entity: Address)(implicit ec: ExecutionContext): Future[Unit] =
    context.update[AddressId, Address](entity)(dSchema, ec)

  override def delete(id: AddressId)(implicit ec: ExecutionContext): Future[Unit] =
    context.delete[AddressId, Address](id)(dSchema, ec)

  override def deleteAll(implicit ec: ExecutionContext): Future[Unit] =
    context.deleteAll(dSchema, ec)

}
