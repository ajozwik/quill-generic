package pl.jozwik.quillgeneric.quillmacro.cassandra.monix

import io.getquill.{ CassandraMonixContext, NamingStrategy }
import monix.eval.Task
import pl.jozwik.quillgeneric.quillmacro.cassandra.monix.CassandraMonixRepository.CassandraMonixContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextUnit
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, Repository, WithId }

object CassandraMonixRepository {
  type CassandraMonixContextDateQuotes[+N <: NamingStrategy] = CassandraMonixContext[N] with MonixWithContextUnit
}

trait CassandraMonixRepository[K, T <: WithId[K], +N <: NamingStrategy] extends Repository[Task, K, T, Unit] with WithCassandraMonixContext[N] {
  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait CassandraMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], +N <: NamingStrategy] extends CassandraMonixRepository[K, T, N]

trait WithCassandraMonixContext[+N <: NamingStrategy] {
  protected val context: CassandraMonixContextDateQuotes[N]
}
