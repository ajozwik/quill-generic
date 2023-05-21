package pl.jozwik.quillgeneric.async

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.jasync.{ JAsyncContext, TransactionalExecutionContext }
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.monad.{ RepositoryMonad, RepositoryMonadWithGeneratedId }
import pl.jozwik.quillgeneric.repository.{ BaseRepository, DateQuotes, WithId }

import scala.concurrent.{ ExecutionContext, Future }

object AsyncJdbcRepository {
  type AsyncJdbcContextDateQuotes[D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection] = JAsyncContext[D, N, C] with DateQuotes
}

trait AsyncJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy, C <: ConcreteConnection]
  extends RepositoryMonadWithGeneratedId[Future, K, T, AsyncJdbcContextDateQuotes[D, N, C], D, N, Long]
  with WithAsyncJdbcContext[K, T, D, N, C] {

  import context.toFuture
  override final def createAndRead(entity: T, generateId: Boolean = true): Future[T] =
    context.transaction { implicit f =>
      for {
        id <- create(entity, generateId)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }

  override final def createOrUpdateAndRead(entity: T, generateId: Boolean = true): Future[T] =
    context.transaction { implicit f =>
      for {
        id <- createOrUpdate(entity, generateId)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }
}

trait AsyncJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy, C <: ConcreteConnection]
  extends RepositoryMonad[Future, K, T, AsyncJdbcContextDateQuotes[D, N, C], D, N, Long]
  with WithAsyncJdbcContext[K, T, D, N, C] {

  import context.toFuture
  override final def createAndRead(entity: T): Future[T] =
    context.transaction { implicit f =>
      for {
        id <- create(entity)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }

  def createOrUpdateAndRead(entity: T): Future[T] =
    context.transaction { implicit f =>
      for {
        id <- createOrUpdate(entity)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }
}

trait WithAsyncJdbcContext[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection] extends BaseRepository[Future, K, T, Long] {
  protected val context: AsyncJdbcContextDateQuotes[D, N, C]

  import context.toFuture

  implicit protected def ec: ExecutionContext

  protected def dynamicSchema: context.DynamicEntityQuery[T]

  def inTransaction[A](f: TransactionalExecutionContext => Future[A]): Future[A] =
    context.transaction(f)

  override final def updateAndRead(entity: T): Future[T] =
    context.transaction { implicit f =>
      for {
        _  <- update(entity)
        el <- readUnsafe(entity.id)
      } yield {
        el
      }
    }

}
