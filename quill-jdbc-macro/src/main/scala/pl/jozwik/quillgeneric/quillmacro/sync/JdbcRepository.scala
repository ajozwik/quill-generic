package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.NamingStrategy
import io.getquill.context.Context
import io.getquill.context.jdbc.JdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId, WithUpdate }

object JdbcRepository {
  type ContextDateQuotes[D <: Idiom, N <: NamingStrategy]        = Context[D, N] with CrudWithContextDateQuotes[Long]
  type JdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy] = JdbcContext[D, N] with ContextDateQuotes[D, N]
}

trait JdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends SyncRepositoryWithGeneratedId[K, T] with WithUpdate[Long] {
  protected val context: JdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait JdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends SyncRepository[K, T] with WithUpdate[Long] {
  protected val context: JdbcContextDateQuotes[D, N]

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait JdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy] extends JdbcRepository[K, T, D, N]
