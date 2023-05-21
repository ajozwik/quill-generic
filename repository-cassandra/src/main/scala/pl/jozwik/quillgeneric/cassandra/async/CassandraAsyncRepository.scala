package pl.jozwik.quillgeneric.cassandra.async

import io.getquill.{ CassandraAsyncContext, NamingStrategy }
import pl.jozwik.quillgeneric.cassandra.async.CassandraAsyncRepository.CassandraAsyncContextDateQuotes
import pl.jozwik.quillgeneric.cassandra.monad.CassandraMonadRepository
import pl.jozwik.quillgeneric.repository.{ DateQuotes, WithId }

import scala.concurrent.Future

object CassandraAsyncRepository {

  type CassandraAsyncContextDateQuotes[+N <: NamingStrategy] = CassandraAsyncContext[N] with DateQuotes
}

trait CassandraAsyncRepository[K, T <: WithId[K], N <: NamingStrategy] extends CassandraMonadRepository[Future, K, T, CassandraAsyncContextDateQuotes[N], N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}
