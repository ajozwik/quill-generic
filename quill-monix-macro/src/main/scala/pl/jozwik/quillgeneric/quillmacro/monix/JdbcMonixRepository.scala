package pl.jozwik.quillgeneric.quillmacro.monix

import io.getquill.NamingStrategy
import io.getquill.context.monix.MonixJdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.monix.JdbcMonixRepository.MonixJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, DateQuotes, WithId }

object JdbcMonixRepository {
  type MonixWithContextDateQuotes                                     = MonixWithContext with DateQuotes
  type MonixJdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = MonixJdbcContext[D, N] with MonixWithContextDateQuotes
}

trait JdbcMonixRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixRepositoryWithGeneratedId[K, T] {
  protected val context: MonixJdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait JdbcMonixRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixRepository[K, T] {
  protected val context: MonixJdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait JdbcMonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends JdbcMonixRepository[K, T, D, N]
