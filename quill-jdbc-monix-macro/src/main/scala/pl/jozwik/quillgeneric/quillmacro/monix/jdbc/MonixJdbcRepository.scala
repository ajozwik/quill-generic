package pl.jozwik.quillgeneric.quillmacro.monix.jdbc

import io.getquill.NamingStrategy
import io.getquill.context.monix.MonixJdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }
import pl.jozwik.quillgeneric.quillmacro.monix.{ MonixRepository, MonixRepositoryWithGeneratedId }
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepository.MonixJdbcContextDateQuotes

object MonixJdbcRepository {
  type MonixJdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = MonixJdbcContext[D, N] with MonixWithContextDateQuotes
}

trait MonixJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixRepository[K, T] {
  protected val context: MonixJdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait MonixJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixRepositoryWithGeneratedId[K, T] {
  protected val context: MonixJdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait MonixJdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixJdbcRepository[K, T, D, N]
