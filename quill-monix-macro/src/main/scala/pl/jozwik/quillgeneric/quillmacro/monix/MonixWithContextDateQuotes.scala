package pl.jozwik.quillgeneric.quillmacro.monix

import pl.jozwik.quillgeneric.quillmacro.DateQuotes

object MonixWithContextDateQuotes {
  type MonixWithContextDateQuotes[U] = MonixWithContext[U] with DateQuotes
}
