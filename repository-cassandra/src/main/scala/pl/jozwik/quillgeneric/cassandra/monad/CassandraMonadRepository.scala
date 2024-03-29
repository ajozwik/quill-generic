package pl.jozwik.quillgeneric.cassandra.monad

import cats.Monad
import cats.implicits.*
import io.getquill.{ CassandraCqlSessionContext, NamingStrategy }
import io.getquill.context.cassandra.CqlIdiom
import pl.jozwik.quillgeneric.monad.RepositoryMonad
import pl.jozwik.quillgeneric.repository.{ Repository, WithId }

trait CassandraMonadRepository[F[_], K, T <: WithId[K], C <: CassandraCqlSessionContext[N], N <: NamingStrategy]
  extends RepositoryMonad[F, K, T, C, CqlIdiom, N, Unit]
  with WithCassandraMonadContext[F, K, T, N, C] {

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

trait WithCassandraMonadContext[F[_], K, T <: WithId[K], +N <: NamingStrategy, C <: CassandraCqlSessionContext[N]] extends Repository[F, K, T, Unit] {
  protected val context: C
  protected implicit def monad: Monad[F]

  override final def update(t: T): F[Unit] =
    for {
      _ <- readUnsafe(t.id)
      _ <- create(t)
    } yield {}
}
