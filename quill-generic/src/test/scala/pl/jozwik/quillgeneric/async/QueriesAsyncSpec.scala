package pl.jozwik.quillgeneric.async

import pl.jozwik.quillgeneric.{ AbstractAsyncSpec, AbstractSpec }

import scala.concurrent.Future

class QueriesAsyncSpec extends AbstractAsyncSpec {

  "QueriesAsync " should {
    "Call all operations " in {
      Future.successful(succeed)
    }
  }
}
