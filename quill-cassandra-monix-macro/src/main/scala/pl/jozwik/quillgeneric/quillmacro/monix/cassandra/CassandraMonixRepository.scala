package pl.jozwik.quillgeneric.quillmacro.monix.cassandra

import io.getquill.{ CassandraMonixContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.monix.cassandra.CassandraMonixRepository.CassandraMonixContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.{ MonixRepository, MonixWithContext }
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, DateQuotes, WithId, WithUpdate }

object CassandraMonixRepository {
  type CassandraMonixContextDateQuotes[N <: NamingStrategy] = CassandraMonixContext[N] with MonixWithContext[Unit] with DateQuotes
}

trait CassandraMonixRepository[K, T <: WithId[K], N <: NamingStrategy] extends MonixRepository[K, T] with WithUpdate[Unit] {
  protected val context: CassandraMonixContextDateQuotes[N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait CassandraMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraMonixRepository[K, T, N]
