package pl.jozwik.quillgeneric.cassandra.monad

import cats.Monad
import cats.implicits.*
import io.getquill.{ CassandraCqlSessionContext, NamingStrategy }
import io.getquill.context.cassandra.CqlIdiom
import pl.jozwik.quillgeneric.monad.RepositoryMonad
import pl.jozwik.quillgeneric.repository.{ CompositeKey, WithId }

trait CassandraMonadRepository[F[_], K, T <: WithId[K], C <: CassandraCqlSessionContext[N], N <: NamingStrategy]
  extends RepositoryMonad[F, K, T, C, CqlIdiom, N, Unit]
  with WithCassandraMonadContext[F, N, C] {
  protected def dynamicSchema: context.DynamicEntityQuery[T]

  override final def updateAndRead(entity: T): F[T] =
    for {
      _  <- update(entity)
      el <- readUnsafe(entity.id)
    } yield {
      el
    }

  override final def createAndRead(entity: T): F[T] =
    for {
      id <- create(entity)
      el <- readUnsafe(id)
    } yield {
      el
    }

  override final def createOrUpdateAndRead(entity: T): F[T] =
    for {
      id <- createOrUpdate(entity)
      el <- readUnsafe(id)
    } yield {
      el
    }

}

trait CassandraMonadRepositoryCompositeKey[F[_], K <: CompositeKey[_, _], C <: CassandraCqlSessionContext[N], T <: WithId[K], N <: NamingStrategy]
  extends CassandraMonadRepository[F, K, T, C, N]

trait WithCassandraMonadContext[F[_], +N <: NamingStrategy, C <: CassandraCqlSessionContext[N]] {
  protected val context: C
  protected implicit val monad: Monad[F]
}
