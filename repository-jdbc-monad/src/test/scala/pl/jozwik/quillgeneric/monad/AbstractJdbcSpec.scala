package pl.jozwik.quillgeneric.monad

import io.getquill.H2JdbcContext
import org.scalatest.{ BeforeAndAfterAll, TryValues }
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.monad.repository.AddressRepository
import pl.jozwik.quillgeneric.repository.DateQuotes

import scala.util.{ Failure, Success, Try }

trait AbstractJdbcSpec extends AbstractSpec with BeforeAndAfterAll with TryValues {

  lazy protected val ctx = new H2JdbcContext(strategy, "h2") with DateQuotes

  protected lazy val addressRepository = new AddressRepository(ctx, "Address")
  implicit class RunUnsafe[T](t: Try[T]) {
    def runUnsafe: T = t match {
      case Success(s) =>
        s
      case Failure(th) =>
        logger.error("", th)
        fail(th)
    }
  }

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
