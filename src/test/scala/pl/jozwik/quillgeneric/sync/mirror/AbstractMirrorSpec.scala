package pl.jozwik.quillgeneric.sync.mirror

import io.getquill.MirrorSqlDialect
import pl.jozwik.quillgeneric.AbstractSpec
import pl.jozwik.quillgeneric.quillmacro.sync.MirrorContextDateQuotes

trait AbstractMirrorSpec extends AbstractSpec {
  lazy protected val ctx = new MirrorContextDateQuotes(MirrorSqlDialect, strategy)
}
