package pl.jozwik.quillgeneric.quillmacro.monix

import io.getquill.NamingStrategy
import io.getquill.context.monix.MonixJdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.monix.MonixRepository.MonixJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, Repository, RepositoryWithGeneratedId, WithId }

object MonixRepository {
  type MonixJdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = MonixJdbcContext[D, N] with MonixWithContext with DateQuotes
}

trait MonixRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends RepositoryWithGeneratedId[K, T] with WithMonix {
  protected val context: MonixJdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait MonixRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends Repository[K, T] with WithMonix {
  protected val context: MonixJdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait MonixRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends MonixRepository[K, T, D, N]
