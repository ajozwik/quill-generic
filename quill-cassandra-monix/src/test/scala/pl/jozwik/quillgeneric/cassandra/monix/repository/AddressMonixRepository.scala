package pl.jozwik.quillgeneric.cassandra.monix.repository

import io.getquill.NamingStrategy
import monix.eval.Task
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.cassandra.monix.CassandraMonixRepository
import CassandraMonixRepository.CassandraMonixContextDateQuotes
import cats.Monad

final class AddressMonixRepository[Naming <: NamingStrategy](
    protected val context: CassandraMonixContextDateQuotes[Naming],
    protected val tableName: String = "Address"
)(implicit protected val monad: Monad[Task])
  extends CassandraMonixRepository[AddressId, Address, Naming] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Address] = {
    context.dynamicQuerySchema[Address](tableName, alias(_.city, "city"))
  }

  private def find(id: AddressId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Task[Seq[Address]] =
    run(dynamicSchema)

  override def create(entity: Address): Task[AddressId] =
    for {
      _ <- run(dynamicSchema.insertValue(entity))
    } yield {
      entity.id
    }

  override def read(id: AddressId): Task[Option[Address]] =
    for {
      seq <- run(dynamicSchema.filter(_.id == lift(id)))
    } yield {
      seq.headOption
    }

  override def createOrUpdate(entity: Address): Task[AddressId] =
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

  override def update(entity: Address): Task[Unit] =
    run(find(entity.id).updateValue(entity))

  override def delete(id: AddressId): Task[Unit] =
    run(find(id).delete)

  override def deleteAll: Task[Unit] =
    run(dynamicSchema.delete)

}
