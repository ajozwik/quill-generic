package pl.jozwik.quillgeneric.quillmacro.async

import com.github.mauricio.async.db.Connection
import io.getquill.NamingStrategy
import io.getquill.context.Context
import io.getquill.context.async.AsyncContext
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.quillmacro.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId, WithUpdate }

object AsyncJdbcRepository {
  type ContextDateQuotes[D <: Idiom, N <: NamingStrategy]                              = Context[D, N] with AsyncCrudWithContext[Long]
  type AsyncJdbcContextDateQuotes[D <: SqlIdiom, N <: NamingStrategy, C <: Connection] = AsyncContext[D, N, C] with ContextDateQuotes[D, N]
}

trait AsyncJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy, C <: Connection]
  extends AsyncRepositoryWithGeneratedId[K, T]
  with WithUpdate[Long]
  with WithAsyncJdbcContext[D, N, C] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]
}

trait AsyncJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy, C <: Connection]
  extends AsyncRepository[K, T]
  with WithUpdate[Long]
  with WithAsyncJdbcContext[D, N, C] {

  protected def dynamicSchema: context.DynamicEntityQuery[T]

}

trait AsyncJdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy, C <: Connection]
  extends AsyncJdbcRepository[K, T, D, N, C]

trait WithAsyncJdbcContext[D <: SqlIdiom, N <: NamingStrategy, C <: Connection] {
  protected val context: AsyncJdbcContextDateQuotes[D, N, C]
}
