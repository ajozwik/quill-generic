package pl.jozwik.quillgeneric.zio

import io.getquill.*
import io.getquill.context.jdbc.{ObjectGenericTimeDecoders, ObjectGenericTimeEncoders}
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.monad.HelperSpec
import pl.jozwik.quillgeneric.repository.DateQuotes
import pl.jozwik.quillgeneric.zio.ZioHelperSpec.environment
import zio.*

import javax.sql.DataSource

object ZioHelperSpec {
  def runLayerUnsafe[T: Tag](layer: ZLayer[Any, Throwable, T]): T =
    zio.Unsafe.unsafe { implicit unsafe =>
      zio.Runtime.default.unsafe.run(zio.Scope.global.extend(layer.build)).getOrThrow()
    }.get

  lazy val environment: ZEnvironment[DataSource] = ZEnvironment(HelperSpec.pool)
}

trait AbstractZioJdbcSpec extends AbstractSpec with BeforeAndAfterAll {

  implicit protected class TaskToTImplicit[T](task: Task[T]) {
    def runUnsafe(): T = unsafe(task)
  }

  protected def unsafe[T](task: Task[T]): T =
    Unsafe.unsafe { implicit unsafe =>
      val io = task.provideEnvironment(environment)
      zio.Runtime.default.unsafe.run(io).getOrThrow()
    }

  lazy protected val ctx = new H2ZioJdbcContext(strategy) with ObjectGenericTimeDecoders with ObjectGenericTimeEncoders with DateQuotes

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
