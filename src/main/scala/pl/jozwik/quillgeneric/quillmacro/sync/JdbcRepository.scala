package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.{ JdbcContextDateQuotes, JdbcCompositeKeyContextDateQuotes }
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

object JdbcRepository {
  type JdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = JdbcContext[D, N] with CrudWithContext with DateQuotes
  type JdbcCompositeKeyContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = JdbcContext[D, N] with CompositeKeyCrudWithContext with DateQuotes
}

trait JdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryWithGeneratedId[K, T] {
  protected val context: JdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait JdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends Repository[K, T] {
  protected val context: JdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait JdbcRepositoryCompositeKey[K1, K2, K <: CompositeKey[K1, K2], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryCompositeKey[K1, K2, K, T] {
  protected val context: JdbcCompositeKeyContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}
