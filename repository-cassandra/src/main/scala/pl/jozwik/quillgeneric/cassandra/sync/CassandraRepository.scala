package pl.jozwik.quillgeneric.cassandra.sync

import io.getquill.{ CassandraSyncContext, NamingStrategy }
import pl.jozwik.quillgeneric.cassandra.monad.CassandraMonadRepository
import pl.jozwik.quillgeneric.cassandra.sync.CassandraRepository.CassandraContextDateQuotes
import pl.jozwik.quillgeneric.repository.{ DateQuotes, WithId }

import scala.util.Try

object CassandraRepository {
  type CassandraContextDateQuotes[+N <: NamingStrategy] = CassandraSyncContext[N] with DateQuotes
}

trait CassandraRepository[K, T <: WithId[K], N <: NamingStrategy]
  extends CassandraMonadRepository[Try, K, T, CassandraContextDateQuotes[N], N]
  with WithCassandraContext[N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait WithCassandraContext[+N <: NamingStrategy] {
  protected val context: CassandraContextDateQuotes[N]
}
