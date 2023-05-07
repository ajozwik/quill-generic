package pl.jozwik.quillgeneric.async

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.Context
import io.getquill.context.jasync.JAsyncContext
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.quillmacro.async.{ AsyncCrudWithContext, AsyncRepository, AsyncRepositoryWithGeneratedId }
import pl.jozwik.quillgeneric.repository.{ CompositeKey, WithId }

import scala.concurrent.{ ExecutionContext, Future }

object AsyncJdbcRepository {
  type ContextDateQuotes[+D <: Idiom, +N <: NamingStrategy]                                     = Context[D, N] with AsyncCrudWithContext[Long]
  type AsyncJdbcContextDateQuotes[D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection] = JAsyncContext[D, N, C] with ContextDateQuotes[D, N]
}

trait AsyncJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncRepositoryWithGeneratedId[K, T, Long]
  with WithAsyncJdbcContext[K, T, D, N, C] {

  import context.toFuture
  override final def createAndRead(entity: T, generateId: Boolean = true)(implicit ex: ExecutionContext): Future[T] =
    context.transaction { f =>
      for {
        id <- create(entity, generateId)(f)
        el <- readUnsafe(id)(f)
      } yield {
        el
      }
    }

  override final def createOrUpdateAndRead(entity: T, generateId: Boolean = true)(implicit ex: ExecutionContext): Future[T] =
    context.transaction { f =>
      for {
        id <- createOrUpdate(entity, generateId)(f)
        el <- readUnsafe(id)(f)
      } yield {
        el
      }
    }
}

trait AsyncJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncRepository[K, T, Long]
  with WithAsyncJdbcContext[K, T, D, N, C] {

  import context.toFuture
  override final def createAndRead(entity: T)(implicit ex: ExecutionContext): Future[T] =
    context.transaction { f =>
      for {
        id <- create(entity)(f)
        el <- readUnsafe(id)(f)
      } yield {
        el
      }
    }

  def createOrUpdateAndRead(entity: T)(implicit ex: ExecutionContext): Future[T] =
    context.transaction { f =>
      for {
        id <- createOrUpdate(entity)(f)
        el <- readUnsafe(id)(f)
      } yield {
        el
      }
    }
}

trait AsyncJdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncJdbcRepository[K, T, D, N, C]

trait WithAsyncJdbcContext[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection] {
  protected val context: AsyncJdbcContextDateQuotes[D, N, C]
  protected def dynamicSchema: context.DynamicEntityQuery[T]

}
