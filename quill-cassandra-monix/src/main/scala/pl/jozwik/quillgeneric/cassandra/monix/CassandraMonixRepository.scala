package pl.jozwik.quillgeneric.cassandra.monix

import io.getquill.{ CassandraMonixContext, NamingStrategy }
import monix.eval.Task
import pl.jozwik.quillgeneric.cassandra.monad.CassandraMonadRepository
import pl.jozwik.quillgeneric.cassandra.monix.CassandraMonixRepository.CassandraMonixContextDateQuotes
import pl.jozwik.quillgeneric.repository.{ DateQuotes, WithId }

object CassandraMonixRepository {
  type CassandraMonixContextDateQuotes[+N <: NamingStrategy] = CassandraMonixContext[N] with DateQuotes
}

trait CassandraMonixRepository[K, T <: WithId[K], N <: NamingStrategy]
  extends CassandraMonadRepository[Task, K, T, CassandraMonixContextDateQuotes[N], N]
  with WithCassandraMonixContext[N]

trait WithCassandraMonixContext[+N <: NamingStrategy] {
  protected val context: CassandraMonixContextDateQuotes[N]
}
