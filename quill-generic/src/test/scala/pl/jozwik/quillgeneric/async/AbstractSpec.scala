package pl.jozwik.quillgeneric.async

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.concurrent.{ AsyncTimeLimitedTests, TimeLimitedTests }
import org.scalatest.time.{ Seconds, Span }
import org.scalatestplus.scalacheck.Checkers
import org.scalatest.{ AsyncWordSpecLike, Matchers, WordSpecLike }

trait AbstractSpecScalaCheck extends AbstractSpec with Checkers

trait Spec extends StrictLogging {
  val TIMEOUT_SECONDS = 600
  val timeLimit = Span(TIMEOUT_SECONDS, Seconds)
}

trait AbstractSpec extends WordSpecLike with TimeLimitedTests with Spec with Matchers

trait AbstractAsyncSpec extends AsyncWordSpecLike with AsyncTimeLimitedTests with Spec with Matchers