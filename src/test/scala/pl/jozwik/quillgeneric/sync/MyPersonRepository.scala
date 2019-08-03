package pl.jozwik.quillgeneric.sync

import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepositoryWithGeneratedId

import scala.util.Try

trait MyPersonRepository[D <: SqlIdiom, N <: NamingStrategy]
  extends JdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {
  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) =>
      p.firstName == lift(name) && p.lastName != lift(""))(offset, limit)(dynamicSchema)
  }

  def max: Try[Option[LocalDate]] = Try {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]] = {
    import context._
    searchByFilter((p: Person) => quote(p.birthDate > lift(date)))(offset, limit)(dynamicSchema)
  }

  def count: Try[Long] = {
    context.count((_: Person) => true)(dynamicSchema)
  }

}
