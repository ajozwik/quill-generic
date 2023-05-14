package pl.jozwik.quillgeneric.monad

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.{ JdbcContext, ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.repository.*

import scala.util.Try

object TryJdbcRepository {
  type JdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = JdbcContext[D, N]
    with DateQuotes
    with ObjectGenericTimeEncoders
    with ObjectGenericTimeDecoders
    with DateQuotes
}

trait TryJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransactionWithGeneratedId[Try, K, T, TryJdbcRepository.JdbcContextDateQuotes[D, N], D, N, Long]
  with WithJdbcContext[K, T, D, N]

trait TryJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransaction[Try, K, T, TryJdbcRepository.JdbcContextDateQuotes[D, N], D, N, Long]
  with WithJdbcContext[K, T, D, N]

trait WithJdbcContext[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends WithTransaction[Try] {
  protected val context: TryJdbcRepository.JdbcContextDateQuotes[D, N]

  protected implicit def monad: Monad[Try]
  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](task: Try[A]): Try[A] =
    context.transaction(task)

}
