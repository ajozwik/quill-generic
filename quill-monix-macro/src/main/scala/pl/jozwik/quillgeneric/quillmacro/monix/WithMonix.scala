package pl.jozwik.quillgeneric.quillmacro.monix

import monix.eval.Task
import pl.jozwik.quillgeneric.quillmacro.WithMonad

trait WithMonix extends WithMonad {
  override type F[A] = Task[A]
}
