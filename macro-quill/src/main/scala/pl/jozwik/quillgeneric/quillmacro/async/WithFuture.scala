package pl.jozwik.quillgeneric.quillmacro.async

import pl.jozwik.quillgeneric.quillmacro.WithMonad

import scala.concurrent.Future

trait WithFuture extends WithMonad {
  override type F[A] = Future[A]
}
