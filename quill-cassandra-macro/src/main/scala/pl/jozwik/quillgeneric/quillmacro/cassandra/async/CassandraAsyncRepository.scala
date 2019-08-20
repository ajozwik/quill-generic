package pl.jozwik.quillgeneric.quillmacro.cassandra.async

import io.getquill.{ CassandraAsyncContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.async.AsyncCrudWithContext.AsyncCrudWithContextDateQuotesUnit
import pl.jozwik.quillgeneric.quillmacro.async.AsyncRepository
import pl.jozwik.quillgeneric.quillmacro.cassandra.async.CassandraAsyncRepository.CassandraAsyncContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId, WithUpdate }

object CassandraAsyncRepository {

  type CassandraAsyncContextDateQuotes[N <: NamingStrategy] = CassandraAsyncContext[N] with AsyncCrudWithContextDateQuotesUnit
}

trait CassandraAsyncRepository[K, T <: WithId[K], N <: NamingStrategy] extends AsyncRepository[K, T] with WithUpdate[Unit] {
  protected val context: CassandraAsyncContextDateQuotes[N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait CassandraAsyncRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraAsyncRepository[K, T, N]
