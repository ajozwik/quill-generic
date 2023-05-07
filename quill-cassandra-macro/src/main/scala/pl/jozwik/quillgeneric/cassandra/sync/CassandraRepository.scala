package pl.jozwik.quillgeneric.cassandra.sync

import cats.Monad
import io.getquill.{CassandraSyncContext, NamingStrategy}
import pl.jozwik.quillgeneric.cassandra.monad.CassandraMonadRepository
import pl.jozwik.quillgeneric.cassandra.sync.CassandraRepository.CassandraContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext.CrudWithContextDateQuotesUnit
import pl.jozwik.quillgeneric.repository.{CompositeKey, WithId}

import scala.util.Try

object CassandraRepository {
  type CassandraContextDateQuotes[+N <: NamingStrategy] = CassandraSyncContext[N] with CrudWithContextDateQuotesUnit
}

trait CassandraRepository[K, T <: WithId[K], N <: NamingStrategy]
  extends CassandraMonadRepository[Try, K, T, CassandraContextDateQuotes[N], N]
  with WithCassandraContext[N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait CassandraRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraRepository[K, T, N]

trait WithCassandraContext[+N <: NamingStrategy] { protected val context: CassandraContextDateQuotes[N]
  protected implicit val monad: Monad[Try] = implicitly[Monad[Try]]
}
