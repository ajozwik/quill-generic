package pl.jozwik.quillgeneric.sync.jdbc.repository

import java.time.Instant

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Address, AddressId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

import scala.util.Try

trait AbstractAddressRepository[D <: SqlIdiom, N <: NamingStrategy] extends JdbcRepositoryWithGeneratedId[AddressId, Address, D, N] {

  def searchUpdateAfter(updated: Instant): Try[Seq[Address]] =
    Try {
      import context._
      val q = dynamicSchema
        .filter(a => quote(a.updated > lift(Option(updated))))
      run(q)

    }
}
