package pl.jozwik.quillgeneric.quillmacro.cassandra.monix

import io.getquill.{ CassandraMonixContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.cassandra.monix.CassandraMonixRepository.CassandraMonixContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, DateQuotes, WithId, WithUpdate }
import pl.jozwik.quillgeneric.quillmacro.monix.{ MonixRepository, MonixWithContext }

object CassandraMonixRepository {
  type CassandraMonixContextDateQuotes[N <: NamingStrategy] = CassandraMonixContext[N] with MonixWithContext[Unit] with DateQuotes
}

trait CassandraMonixRepository[K, T <: WithId[K], N <: NamingStrategy] extends MonixRepository[K, T] with WithUpdate[Unit] {
  protected val context: CassandraMonixContextDateQuotes[N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait CassandraMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraMonixRepository[K, T, N]
