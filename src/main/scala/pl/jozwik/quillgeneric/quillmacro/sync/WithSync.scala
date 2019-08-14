package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.WithMonad

import scala.util.Try

trait WithSync extends WithMonad {
  override type F[_] = Try[_]
}
