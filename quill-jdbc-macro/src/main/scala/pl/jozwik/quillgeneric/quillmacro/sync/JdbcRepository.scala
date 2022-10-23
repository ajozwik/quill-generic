package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.NamingStrategy
import io.getquill.context.Context
import io.getquill.context.jdbc.{ JdbcContext, ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.util.Try

object JdbcRepository {
  type ContextDateQuotes[D <: Idiom, N <: NamingStrategy] = Context[D, N] with CrudWithContextDateQuotes[Long]
  type JdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = JdbcContext[D, N]
    with ContextDateQuotes[D, N]
    with ObjectGenericTimeEncoders
    with ObjectGenericTimeDecoders
}

trait JdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends SyncRepositoryWithGeneratedIdWithTransaction[K, T, Long]
  with WithJdbcContext[D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](task: Try[A]): Try[A] =
    context.transaction(task)
}

trait JdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends SyncRepositoryWithTransaction[K, T, Long] with WithJdbcContext[D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](task: Try[A]): Try[A] =
    context.transaction(task)

}

trait JdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends JdbcRepository[K, T, D, N]

trait WithJdbcContext[D <: SqlIdiom, N <: NamingStrategy] {
  protected val context: JdbcContextDateQuotes[D, N]
}
