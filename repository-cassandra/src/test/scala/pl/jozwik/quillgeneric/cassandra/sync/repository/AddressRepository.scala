package pl.jozwik.quillgeneric.cassandra.sync.repository

import io.getquill.NamingStrategy
import pl.jozwik.quillgeneric.cassandra.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.cassandra.sync.CassandraRepository
import CassandraRepository.CassandraContextDateQuotes
import cats.Monad

import scala.util.Try

final class AddressRepository[Naming <: NamingStrategy](
    protected val context: CassandraContextDateQuotes[Naming],
    protected val tableName: String = "Address"
)(implicit protected val monad: Monad[Try])
  extends CassandraRepository[AddressId, Address, Naming] {
  import context.*
  protected lazy val dynamicSchema: context.DynamicEntityQuery[Address] = {
    context.dynamicQuerySchema[Address](tableName, alias(_.city, "city"))
  }

  private def find(id: AddressId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Try[Seq[Address]] =
    Try(run(dynamicSchema))

  override def create(entity: Address): Try[AddressId] =
    for {
      _ <- Try(run(dynamicSchema.insertValue(entity)))
    } yield {
      entity.id
    }

  override def read(id: AddressId): Try[Option[Address]] = {
    val q = dynamicSchema.filter(_.id == lift(id))
    for {
      seq <- Try(run(q))
    } yield {
      seq.headOption
    }
  }

  override def createOrUpdate(entity: Address): Try[AddressId] =
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

  override def delete(id: AddressId): Try[Unit] = Try {
    run(find(id).delete)
  }

  override def deleteAll: Try[Unit] =
    Try(run(dynamicSchema.delete))

}
