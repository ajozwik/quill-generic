package pl.jozwik.quillgeneric.async

import com.github.jasync.sql.db.ConcreteConnection
import io.getquill.NamingStrategy
import io.getquill.context.jasync.JAsyncContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.async.AsyncJdbcRepository.AsyncJdbcContextDateQuotes
import pl.jozwik.quillgeneric.repository.{ AsyncRepository, AsyncRepositoryWithGeneratedId, CompositeKey, DateQuotes, WithId }

import scala.concurrent.Future

object AsyncJdbcRepository {
  type AsyncJdbcContextDateQuotes[D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection] = JAsyncContext[D, N, C] with DateQuotes
}

trait AsyncJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncRepositoryWithGeneratedId[K, T, Long]
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

trait AsyncJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncRepository[K, T, Long]
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

trait AsyncJdbcRepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection]
  extends AsyncJdbcRepository[K, T, D, N, C]

trait WithAsyncJdbcContext[K, T <: WithId[K], D <: SqlIdiom, +N <: NamingStrategy, C <: ConcreteConnection] {
  protected val context: AsyncJdbcContextDateQuotes[D, N, C]
  protected def dynamicSchema: context.DynamicEntityQuery[T]

}
