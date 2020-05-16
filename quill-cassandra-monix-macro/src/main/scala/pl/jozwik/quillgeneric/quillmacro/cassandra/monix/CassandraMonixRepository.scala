package pl.jozwik.quillgeneric.quillmacro.cassandra.monix

import io.getquill.{ CassandraMonixContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.cassandra.monix.CassandraMonixRepository.CassandraMonixContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.MonixRepository
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextUnit
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId, WithUpdate }

object CassandraMonixRepository {
  type CassandraMonixContextDateQuotes[N <: NamingStrategy] = CassandraMonixContext[N] with MonixWithContextUnit
}

trait CassandraMonixRepository[K, T <: WithId[K], N <: NamingStrategy] extends MonixRepository[K, T] with WithUpdate[Unit] with WithCassandraMonixContext[N] {
  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait CassandraMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraMonixRepository[K, T, N]

trait WithCassandraMonixContext[N <: NamingStrategy] {
  protected val context: CassandraMonixContextDateQuotes[N]
}
