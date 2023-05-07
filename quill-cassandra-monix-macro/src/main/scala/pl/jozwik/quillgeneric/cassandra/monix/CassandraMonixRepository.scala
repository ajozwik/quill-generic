package pl.jozwik.quillgeneric.cassandra.monix

import cats.Monad
import io.getquill.{ CassandraMonixContext, NamingStrategy }
import monix.eval.Task
import pl.jozwik.quillgeneric.cassandra.monad.CassandraMonadRepository
import pl.jozwik.quillgeneric.cassandra.monix.CassandraMonixRepository.CassandraMonixContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextUnit
import pl.jozwik.quillgeneric.repository.{ CompositeKey, WithId }

object CassandraMonixRepository {
  type CassandraMonixContextDateQuotes[+N <: NamingStrategy] = CassandraMonixContext[N] with MonixWithContextUnit
}

trait CassandraMonixRepository[K, T <: WithId[K], N <: NamingStrategy]
  extends CassandraMonadRepository[Task, K, T, CassandraMonixContextDateQuotes[N], N]
  with WithCassandraMonixContext[N]
trait CassandraMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], N <: NamingStrategy] extends CassandraMonixRepository[K, T, N]

trait WithCassandraMonixContext[+N <: NamingStrategy] {
  protected val context: CassandraMonixContextDateQuotes[N]
  protected implicit val monad: Monad[Task] = implicitly[Monad[Task]]

}
