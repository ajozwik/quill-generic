package pl.jozwik.quillgeneric.quillmacro.cassandra.sync

import io.getquill.{ CassandraSyncContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.cassandra.sync.CassandraRepository.CassandraContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext.CrudWithContextDateQuotesUnit
import pl.jozwik.quillgeneric.quillmacro.sync.SyncRepository
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

object CassandraRepository {
  type CassandraContextDateQuotes[+N <: NamingStrategy] = CassandraSyncContext[N] with CrudWithContextDateQuotesUnit
}

trait CassandraRepository[K, T <: WithId[K], +N <: NamingStrategy] extends SyncRepository[K, T, Unit] with WithCassandraContext[N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait CassandraRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], +N <: NamingStrategy] extends CassandraRepository[K, T, N]

trait WithCassandraContext[+N <: NamingStrategy] { protected val context: CassandraContextDateQuotes[N] }
