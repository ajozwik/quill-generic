package pl.jozwik.quillgeneric.sync.jdbc.repository

import java.time.Instant

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Sale, SaleId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryCompositeKey

import scala.util.Try

trait AbstractSaleRepository[D <: SqlIdiom, N <: NamingStrategy] extends JdbcRepositoryCompositeKey[SaleId, Sale, D, N] {

  import context._

  def searchFrom(from: Instant): Try[Seq[Sale]] = Try {
    searchByFilter[Sale](p => p.saleDate >= lift(from))(0, Int.MaxValue)(dynamicSchema)
  }
}
