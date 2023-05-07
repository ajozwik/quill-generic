package pl.jozwik.quillgeneric.monad

import cats.Monad
object MonadHelper {
  def pure[F[_], T](a: T)(implicit m: Monad[F]): F[T] = m.pure(a)
}
