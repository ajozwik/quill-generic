package pl.jozwik.quillgeneric.cassandra.async.repository

import io.getquill.NamingStrategy
import pl.jozwik.quillgeneric.cassandra.model.{Address, AddressId}
import pl.jozwik.quillgeneric.cassandra.async.CassandraAsyncRepository
import pl.jozwik.quillgeneric.cassandra.async.CassandraAsyncRepository.CassandraAsyncContextDateQuotes

import scala.concurrent.{ExecutionContext, Future}

final class AddressRepositoryGen[Naming <: NamingStrategy](
    protected val context: CassandraAsyncContextDateQuotes[Naming],
    protected val tableName: String = "Address"
)(implicit protected val ec: ExecutionContext) extends CassandraAsyncRepository[AddressId, Address, Naming] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Address] = {
    context.dynamicQuerySchema[Address](tableName)
  }

  private def find(id: AddressId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Future[Seq[Address]] =
    run(dynamicSchema)

  override def create(entity: Address): Future[AddressId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def createOrUpdate(entity: Address): Future[AddressId] =
    for {
      el <- read(entity.id)
      id <- el match {
        case None =>
          create(entity)
        case _ =>
          pure(entity.id)
      }
    } yield {
      id
    }

  override def read(id: AddressId): Future[Option[Address]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }

  override def update(entity: Address): Future[Unit] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: AddressId): Future[Unit] =
    run(find(id).delete)

  override def deleteAll: Future[Unit] =
    run(dynamicSchema.delete)

}
