package pl.jozwik.quillgeneric

import io.getquill.NamingStrategy
import io.getquill.context.jdbc.{ObjectGenericTimeDecoders, ObjectGenericTimeEncoders}
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.doobie.DoobieContextBase

package object doobie {
  type DoobieJdbcContextWithDateQuotes[+Dialect <: SqlIdiom, +Naming <: NamingStrategy] = DoobieContextBase[Dialect, Naming]
    with ObjectGenericTimeDecoders
    with ObjectGenericTimeEncoders
}
