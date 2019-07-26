package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository

import scala.util.Try

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends JdbcRepository[PersonId, Person, Dialect, Naming] {

  def searchByFirstName(name: String): Try[Seq[Person]] = Try {
    import context._
    val q = dynamicSchema.withFilter(_.firstName == lift(name)).filter(_.lastName != lift(""))
    run(q)
  }

  def max: Try[Option[LocalDate]] = Try {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }
}