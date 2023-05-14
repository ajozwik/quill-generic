package pl.jozwik.quillgeneric.monad.repository

import java.time.Instant
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Sale, SaleId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryCompositeKey

import scala.util.Try

trait AbstractSaleRepository[D <: SqlIdiom, N <: NamingStrategy] extends TryJdbcRepositoryCompositeKey[SaleId, Sale, D, N] {

  import context.*

  def searchFrom(from: Instant): Try[Seq[Sale]] = Try {
    run(dynamicSchema.filter(p => quote(p.saleDate >= lift(from))).drop(0).take(Int.MaxValue))
  }
}
