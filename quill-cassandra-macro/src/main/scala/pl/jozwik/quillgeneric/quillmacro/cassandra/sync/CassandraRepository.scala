package pl.jozwik.quillgeneric.quillmacro.cassandra.sync

import io.getquill.{ CassandraSyncContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.cassandra.sync.CassandraRepository.CassandraContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.{ CrudWithContext, SyncRepository }
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, DateQuotes, WithId, WithUpdate }

object CassandraRepository {
  type CassandraContextDateQuotes[N <: NamingStrategy] = CassandraSyncContext[N] with CrudWithContext[Unit] with DateQuotes
}

trait CassandraRepository[K, T <: WithId[K], N <: NamingStrategy] extends SyncRepository[K, T] with WithUpdate[Unit] {
  protected val context: CassandraContextDateQuotes[N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait CassandraMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraRepository[K, T, N]
