package pl.jozwik.quillgeneric.monad

import cats.implicits.*
import io.getquill.context.Context
import io.getquill.idiom.Idiom
import io.getquill.NamingStrategy
import pl.jozwik.quillgeneric.repository.{ RepositoryWithTransactionWithGeneratedId, WithId, WithTransaction }

trait RepositoryMonadWithTransactionWithGeneratedId[F[_], K, T <: WithId[K], C <: Context[D, N], +D <: Idiom, +N <: NamingStrategy, UP]
  extends RepositoryWithTransactionWithGeneratedId[F, K, T, UP]
  with RepositoryMonadBaseWithTransaction[F, K, T, C, D, N, UP] {

  override final def createAndRead(entity: T, generateId: Boolean = true): F[T] =
    inTransaction {
      for {
        id <- create(entity, generateId)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }

  override final def createOrUpdateAndRead(entity: T, generateId: Boolean = true): F[T] =
    inTransaction {
      for {
        id <- createOrUpdate(entity, generateId)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }

}

trait RepositoryMonadWithTransaction[F[_], K, T <: WithId[K], C <: Context[D, N], +D <: Idiom, +N <: NamingStrategy, UP]
  extends RepositoryMonad[F, K, T, C, D, N, UP]
  with RepositoryMonadBaseWithTransaction[F, K, T, C, D, N, UP] {

  override final def createAndRead(entity: T): F[T] =
    inTransaction {
      for {
        id <- create(entity)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }

  override final def createOrUpdateAndRead(entity: T): F[T] =
    inTransaction {
      for {
        id <- createOrUpdate(entity)
        el <- readUnsafe(id)
      } yield {
        el
      }
    }

}

trait RepositoryMonadBaseWithTransaction[F[_], K, T <: WithId[K], C <: Context[D, N], +D <: Idiom, +N <: NamingStrategy, UP]
  extends RepositoryMonadBase[F, K, T, C, D, N, UP]
  with WithTransaction[F] {

  override final def updateAndRead(entity: T): F[T] =
    inTransaction {
      for {
        _  <- update(entity)
        el <- readUnsafe(entity.id)
      } yield {
        el
      }
    }
}
