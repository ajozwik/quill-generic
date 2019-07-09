package pl.jozwik.quillgeneric

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.concurrent.{ AsyncTimeLimitedTests, TimeLimitedTests }
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.{ AsyncWordSpecLike, BeforeAndAfterAll, Matchers, WordSpecLike }
import org.scalatestplus.scalacheck.Checkers

trait AbstractSpecScalaCheck extends AbstractSpec with Checkers

trait Spec extends StrictLogging {
  val TIMEOUT_SECONDS = 600
  val timeLimit = Span(TIMEOUT_SECONDS, Seconds)
}

trait AbstractSpec extends WordSpecLike with TimeLimitedTests with Spec with Matchers with BeforeAndAfterAll

trait AbstractAsyncSpec extends AsyncWordSpecLike with AsyncTimeLimitedTests with Spec with Matchers