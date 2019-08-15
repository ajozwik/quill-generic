package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext.ContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

object JdbcRepository {
  type JdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = JdbcContext[D, N] with ContextDateQuotes[D, N]
}

trait JdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends SyncRepositoryWithGeneratedId[K, T] {
  protected val context: JdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait JdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends SyncRepository[K, T] {
  protected val context: JdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait JdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends JdbcRepository[K, T, D, N]
