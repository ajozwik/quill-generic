package pl.jozwik.quillgeneric

import io.getquill.{ NamingStrategy, SnakeCase, UpperCase }

object Strategy {
  val namingStrategy: NamingStrategy = NamingStrategy(SnakeCase, UpperCase)
}
