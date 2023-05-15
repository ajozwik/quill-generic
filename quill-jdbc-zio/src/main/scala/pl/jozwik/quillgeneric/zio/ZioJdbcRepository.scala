package pl.jozwik.quillgeneric.zio

import io.getquill.*
import io.getquill.context.ZioJdbc.QIO
import io.getquill.context.jdbc.{ ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import io.getquill.context.qzio.ZioJdbcContext
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.monad.*
import pl.jozwik.quillgeneric.repository.*
import zio.Task
object ZioJdbcRepository {
  type ZioJdbcContextWithDateQuotes[+Dialect <: SqlIdiom, +Naming <: NamingStrategy] = ZioJdbcContext[Dialect, Naming]
    with ObjectGenericTimeDecoders
    with ObjectGenericTimeEncoders
}
trait ZioJdbcRepositoryWithGeneratedId[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransactionWithGeneratedId[Task, K, T, ZioJdbcRepository.ZioJdbcContextWithDateQuotes[D, N], D, N, Long]
  with ZioJdbcWithTransaction[K, T, D, N]
trait ZioJdbcRepository[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadWithTransaction[Task, K, T, ZioJdbcRepository.ZioJdbcContextWithDateQuotes[D, N], D, N, Long]
  with ZioJdbcWithTransaction[K, T, D, N]

@SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
trait ZioJdbcWithTransaction[K, T <: WithId[K], D <: SqlIdiom, N <: NamingStrategy]
  extends RepositoryMonadBaseWithTransaction[Task, K, T, ZioJdbcRepository.ZioJdbcContextWithDateQuotes[D, N], D, N, Long] {

  override final def inTransaction[A](task: Task[A]): Task[A] =
    context.transaction(task.asInstanceOf[QIO[A]]).asInstanceOf[Task[A]]

  protected implicit def toTask[A](t: QIO[A]): Task[A] =
    t.asInstanceOf[Task[A]]

  protected implicit def fromTask[A](t: Task[A]): QIO[A] =
    t.asInstanceOf[QIO[A]]

}
