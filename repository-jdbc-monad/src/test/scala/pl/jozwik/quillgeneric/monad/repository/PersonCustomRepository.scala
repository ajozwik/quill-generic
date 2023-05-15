package pl.jozwik.quillgeneric.monad.repository

import cats.Monad
import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.monad.TryJdbcRepository.JdbcContextDateQuotes
import pl.jozwik.quillgeneric.monad.TryJdbcRepositoryWithGeneratedId

import scala.util.Try

final class PersonCustomRepository[D <: SqlIdiom, N <: NamingStrategy](protected val context: JdbcContextDateQuotes[D, N], tableName: String)(implicit
    protected val monad: Monad[Try]
) extends TryJdbcRepositoryWithGeneratedId[PersonId, Person, D, N] {
  import context.*

  private val aliases =
    Seq {
      alias[Person](_.birthDate, "dob")
    }

  protected lazy val dynamicSchema: context.DynamicEntityQuery[Person] = context.dynamicQuerySchema[Person](tableName, aliases*)

  private def find(id: PersonId) =
    dynamicSchema.filter(_.id == lift(id))

  override def all: Try[Seq[Person]] =
    Try {
      run(dynamicSchema)
    }

  override def create(entity: Person, generateId: Boolean = true): Try[PersonId] =
    Try {
      if (generateId) {
        run(dynamicSchema.insertValue(entity).returningGenerated(_.id))
      } else {
        run(dynamicSchema.insertValue(entity).returning(_.id))
      }
    }

  override def read(id: PersonId): Try[Option[Person]] =
    for {
      seq <- Try(run(dynamicSchema.filter(_.id == lift(id))))
    } yield {
      seq.headOption
    }

  override def createOrUpdate(entity: Person, generateId: Boolean = true): Try[PersonId] =
    inTransaction {
      for {
        el <- Try(run(find(entity.id).updateValue(entity)))
        id <- el match {
          case 0 =>
            create(entity, generateId)
          case _ =>
            pure(entity.id)
        }
      } yield {
        id
      }
    }

  override def update(entity: Person): Try[Long] =
    Try {
      run(find(entity.id).updateValue(entity))
    }

  override def delete(id: PersonId): Try[Long] =
    Try {
      run(find(id).delete)
    }

  override def deleteAll: Try[Long] =
    Try {
      run(dynamicSchema.delete)
    }

}
