package pl.jozwik.quillgeneric.monix

import io.getquill.H2MonixJdbcContext
import monix.execution.Scheduler
import org.scalatest.BeforeAndAfterAll
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextLong

trait AbstractMonixJdbcSpec extends AbstractSpec with BeforeAndAfterAll {
  protected implicit val scheduler: Scheduler = Scheduler.Implicits.global
  lazy protected val ctx                      = new H2MonixJdbcContext(strategy, "h2Monix") with MonixWithContextLong

  override def afterAll(): Unit = {
    ctx.close()
    super.afterAll()
  }

}
