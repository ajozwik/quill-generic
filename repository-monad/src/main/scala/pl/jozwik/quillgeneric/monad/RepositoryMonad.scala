package pl.jozwik.quillgeneric.monad

import cats.Monad
import cats.implicits.*
import io.getquill.NamingStrategy
import io.getquill.context.Context
import io.getquill.idiom.Idiom
import pl.jozwik.quillgeneric.repository.{ BaseRepository, Repository, RepositoryWithGeneratedId, WithId }

trait RepositoryMonadWithGeneratedId[F[_], K, T <: WithId[K], C <: Context[D, N], +D <: Idiom, +N <: NamingStrategy, UP]
  extends RepositoryWithGeneratedId[F, K, T, UP]
  with RepositoryMonadBase[F, K, T, C, D, N, UP]

trait RepositoryMonad[F[_], K, T <: WithId[K], C <: Context[D, N], +D <: Idiom, +N <: NamingStrategy, UP]
  extends Repository[F, K, T, UP]
  with RepositoryMonadBase[F, K, T, C, D, N, UP]

trait RepositoryMonadBase[F[_], K, T <: WithId[K], C <: Context[D, N], +D <: Idiom, +N <: NamingStrategy, UP] extends BaseRepository[F, K, T, UP] {
  protected implicit def monad: Monad[F]

  protected val context: C

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  override final def readUnsafe(id: K): F[T] =
    for {
      opt <- read(id)
    } yield {
      opt.getOrElse(throw new NoSuchElementException(s"$id"))
    }

  protected final def pure[E](el: E): F[E] = MonadHelper.pure[F, E](el)
}
