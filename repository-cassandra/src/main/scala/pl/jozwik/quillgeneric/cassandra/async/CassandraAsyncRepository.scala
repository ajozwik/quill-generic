package pl.jozwik.quillgeneric.cassandra.async

import io.getquill.{CassandraAsyncContext, NamingStrategy}
import pl.jozwik.quillgeneric.cassandra.async.CassandraAsyncRepository.CassandraAsyncContextDateQuotes
import pl.jozwik.quillgeneric.repository.{AsyncRepository, CompositeKey, DateQuotes, WithId}

import scala.concurrent.{ExecutionContext, Future}

object CassandraAsyncRepository {

  type CassandraAsyncContextDateQuotes[+N <: NamingStrategy] = CassandraAsyncContext[N] with DateQuotes
}

trait CassandraAsyncRepository[K, T <: WithId[K], +N <: NamingStrategy] extends AsyncRepository[K, T, Unit] with WithCassandraAsyncContext[N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  override final def createAndRead(entity: T)(implicit ex: ExecutionContext): Future[T] =
      for {
        id <- create(entity)
        el <- readUnsafe(entity.id)
      } yield {
        el
      }

  override final def createOrUpdateAndRead(entity: T)(implicit ex: ExecutionContext): Future[T] =
      for {
        id <- createOrUpdate(entity)
        el <- readUnsafe(id)
      } yield {
        el
      }


}

trait CassandraAsyncRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], +N <: NamingStrategy] extends CassandraAsyncRepository[K, T, N]

trait WithCassandraAsyncContext[+N <: NamingStrategy] {
  protected val context: CassandraAsyncContextDateQuotes[N]
}
