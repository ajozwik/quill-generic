package pl.jozwik.quillgeneric.monix.jdbc

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.{ ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import io.getquill.context.monix.MonixJdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.quillgeneric.monad.{ RepositoryMonadWithTransaction, RepositoryMonadWithTransactionWithGeneratedId }
import pl.jozwik.quillgeneric.monix.jdbc.MonixJdbcRepository.MonixJdbcContextDateQuotes
import pl.jozwik.quillgeneric.repository.{ BaseRepository, DateQuotes, WithId }

object MonixJdbcRepository {
  type MonixJdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = MonixJdbcContext[D, N]
    with DateQuotes
    with ObjectGenericTimeEncoders
    with ObjectGenericTimeDecoders
}

trait MonixJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransaction[Task, K, T, MonixJdbcContextDateQuotes[D, N], D, N, Long]
  with WithMonixJdbcContext[K, T, D, N]

trait MonixJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransactionWithGeneratedId[Task, K, T, MonixJdbcContextDateQuotes[D, N], D, N, Long]
  with WithMonixJdbcContext[K, T, D, N] {}

trait WithMonixJdbcContext[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] {
  this: BaseRepository[Task, K, T, Long] =>
  protected val context: MonixJdbcContextDateQuotes[D, N]
  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](task: Task[A]): Task[A] =
    context.transaction(task)

}
