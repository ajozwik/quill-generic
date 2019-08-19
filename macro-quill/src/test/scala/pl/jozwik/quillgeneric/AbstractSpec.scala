package pl.jozwik.quillgeneric

import java.time.{ LocalDate, LocalDateTime }
import com.typesafe.scalalogging.StrictLogging
import org.scalatest.concurrent.{ AsyncTimeLimitedTests, TimeLimitedTests }
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ AsyncWordSpecLike, BeforeAndAfterAll, Matchers, WordSpecLike }
import org.scalatestplus.scalacheck.Checkers
import pl.jozwik.quillgeneric.model.AddressId

trait AbstractSpecScalaCheck extends AbstractSpec with Checkers

trait Spec extends StrictLogging {
  val TIMEOUT_SECONDS = 600
  val timeLimit       = Span(TIMEOUT_SECONDS, Seconds)
}

trait AbstractSpec extends WordSpecLike with TimeLimitedTests with Spec with Matchers with BeforeAndAfterAll {

  protected val now: LocalDateTime = LocalDateTime.now()
  protected val today: LocalDate   = now.toLocalDate
  protected val strategy           = Strategy.namingStrategy

  protected val (offset, limit) = (0, 100)
  protected val generateId      = true
  protected val addressId       = AddressId(1)
}

trait AbstractAsyncSpec extends AsyncWordSpecLike with AsyncTimeLimitedTests with Spec with Matchers