package pl.jozwik.quillgeneric.doobie

import doobie.ConnectionIO
import io.getquill.NamingStrategy
import io.getquill.context.jdbc.{ ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import io.getquill.context.sql.idiom.SqlIdiom
import io.getquill.doobie.DoobieContextBase
import pl.jozwik.quillgeneric.doobie.DoobieRepository.DoobieJdbcContextWithDateQuotes
import pl.jozwik.quillgeneric.monad.*
import pl.jozwik.quillgeneric.repository.*
object DoobieRepository {
  type DoobieJdbcContextWithDateQuotes[+Dialect <: SqlIdiom, +Naming <: NamingStrategy] = DoobieContextBase[Dialect, Naming]
    with ObjectGenericTimeDecoders
    with ObjectGenericTimeEncoders
    with DateQuotes
}

trait DoobieRepositoryWithTransactionWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransactionWithGeneratedId[ConnectionIO, K, T, DoobieJdbcContextWithDateQuotes[D, N], D, N, Long]
  with DoobieJdbcTransaction[K, T, D, N]

trait DoobieRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransaction[ConnectionIO, K, T, DoobieJdbcContextWithDateQuotes[D, N], D, N, Long]
  with DoobieJdbcTransaction[K, T, D, N]

trait DoobieJdbcTransaction[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadBaseWithTransaction[ConnectionIO, K, T, DoobieJdbcContextWithDateQuotes[D, N], D, N, Long] {

  override final def inTransaction[A](task: ConnectionIO[A]): ConnectionIO[A] =
    task

}
