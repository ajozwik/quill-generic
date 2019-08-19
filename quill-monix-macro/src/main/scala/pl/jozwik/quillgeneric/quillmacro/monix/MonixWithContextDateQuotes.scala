package pl.jozwik.quillgeneric.quillmacro.monix

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.DateQuotes

trait MonixWithContextDateQuotes[U] extends MonixWithContext[U] with DateQuotes {
  this: Context[_, _] =>
}

object MonixWithContextDateQuotes {
  type MonixWithContextDateQuotesUnit = MonixWithContextDateQuotes[Unit]
  type MonixWithContextDateQuotesLong = MonixWithContextDateQuotes[Long]
}
