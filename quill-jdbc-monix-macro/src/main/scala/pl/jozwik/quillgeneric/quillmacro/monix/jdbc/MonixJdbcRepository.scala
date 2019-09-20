package pl.jozwik.quillgeneric.quillmacro.monix.jdbc

import io.getquill.NamingStrategy
import io.getquill.context.monix.MonixJdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import monix.eval.Task
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextDateQuotesLong
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepository.MonixJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.{ MonixRepositoryLongWithTransaction, MonixRepositoryWithGeneratedIdLongWithTransaction }
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

object MonixJdbcRepository {
  type MonixJdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = MonixJdbcContext[D, N] with MonixWithContextDateQuotesLong
}

trait MonixJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends MonixRepositoryLongWithTransaction[K, T]
  with WithMonixJdbcContext[D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](task: Task[A]): Task[A] =
    context.transaction(task)

}

trait MonixJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends MonixRepositoryWithGeneratedIdLongWithTransaction[K, T]
  with WithMonixJdbcContext[D, N] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](task: Task[A]): Task[A] =
    context.transaction(task)
}

trait WithMonixJdbcContext[D <: SqlIdiom, N <: NamingStrategy] {
  protected val context: MonixJdbcContextDateQuotes[D, N]
}

trait MonixJdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixJdbcRepository[K, T, D, N]
