package pl.jozwik.quillgeneric.sync.jdbc.repository

import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

import scala.util.Try

trait MyPersonRepository[D <: SqlIdiom, N <: NamingStrategy] extends JdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {

  import context._

  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = Try {
    searchByFilter((p: Person) => p.firstName == lift(name) && p.lastName != lift(""))(offset, limit)(dynamicSchema)
  }

  def max: Try[Option[LocalDate]] = Try {
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]] = Try {
    searchByFilter((p: Person) => quote(p.birthDate > lift(date)))(offset, limit)(dynamicSchema)
  }

  def count: Try[Long] = Try {
    context.count((_: Person) => true)(dynamicSchema)
  }

}
