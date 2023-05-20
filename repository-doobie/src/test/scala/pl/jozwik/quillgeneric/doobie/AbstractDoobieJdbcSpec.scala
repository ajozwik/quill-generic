package pl.jozwik.quillgeneric.doobie

import cats.Monad
import cats.effect.IO
import cats.effect.kernel.Resource
import doobie.ConnectionIO
import doobie.h2.*
import doobie.implicits.*
import io.getquill.context.jdbc.{ ObjectGenericTimeDecoders, ObjectGenericTimeEncoders }
import io.getquill.doobie.DoobieContext
import org.scalatest.{ BeforeAndAfterAll, EitherValues }
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.repository.DateQuotes

import scala.concurrent.ExecutionContext

trait AbstractDoobieJdbcSpec extends AbstractSpec with BeforeAndAfterAll with EitherValues {

  import cats.effect.unsafe.implicits.global

  implicit protected val monad: Monad[ConnectionIO] = implicitly[Monad[ConnectionIO]]

  implicit class DoobieUnsafeToT[T](task: ConnectionIO[T]) {
    def runUnsafe(): T = transactor
      .use { xa =>
        task.transact(xa)
      }
      .unsafeRunSync()
  }

  private lazy val transactor: Resource[IO, H2Transactor[IO]] =
    H2Transactor.newH2Transactor[IO](
      "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:scripts/create.sql'",
      "sa",
      "",
      ExecutionContext.global
    )

  lazy protected val ctx = new DoobieContext.H2(strategy) with ObjectGenericTimeDecoders with ObjectGenericTimeEncoders with DateQuotes

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
