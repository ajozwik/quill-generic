package pl.jozwik.quillgeneric.monad.repository

import java.time.LocalDate
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryWithGeneratedId

import scala.util.Try

trait MyPersonRepository[D <: SqlIdiom, N <: NamingStrategy] extends TryJdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {

  import context.*

  def searchByFirstName(name: String)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = dynamicSchema.filter(_.firstName == lift(name)).filter(_.lastName != lift("")).drop(offset).take(limit)
    Try {
      run(q)
    }
  }

  def max: Try[Option[LocalDate]] = Try {
    val r = dynamicSchema.map(p => p.birthDate)
    run(r.max)
  }

  def youngerThan(date: LocalDate)(offset: Int, limit: Int): Try[Seq[Person]] = {
    val q = dynamicSchema.filter(p => quote(p.birthDate > lift(date))).drop(offset).take(limit)
    Try {
      run(q)
    }
  }

  def count: Try[Long] = Try {
    run(dynamicSchema.size)
  }

  def batchUpdate(persons: Seq[Person]): Try[Unit] = Try {
    context.transaction {
      persons.foreach { p =>
        context.run(dynamicSchema.insertValue(p))
      }
    }
  }

}
