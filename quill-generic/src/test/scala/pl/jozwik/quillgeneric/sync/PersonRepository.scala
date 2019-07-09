package pl.jozwik.quillgeneric.sync

import io.getquill.{ H2JdbcContext, SnakeCase }
import pl.jozwik.quillgeneric.model.{ Person, PersonId }

import scala.util.Try

class PersonRepository(ctx: H2JdbcContext[SnakeCase.type] with Queries) extends Repository[PersonId, Person] {

  import ctx._

  override def all: Try[Seq[Person]] =
    ctx.all[Person]

  override def create(person: Person, generateId: Boolean = false): Try[PersonId] =
    if (generateId) {
      ctx.createAndGenerateId[PersonId, Person](person)
    } else {
      ctx.create[PersonId, Person](person)
    }

  override def read(id: PersonId): Try[Option[Person]] =
    ctx.read[PersonId, Person](id)

  override def createOrUpdate(entity: Person): Try[PersonId] =
    ctx.insertOrUpdate[PersonId, Person](entity)

  override def update(person: Person): Try[Long] =
    ctx.update[Person](person)

  override def update(id: PersonId, action: Person => (Any, Any), actions: Function[Person, (Any, Any)]*): Try[Long] =
    ctx.updateById[Person](_.id == lift(id), action, actions: _*)

  override def delete(id: PersonId): Try[Boolean] =
    ctx.deleteByFilter[Person](_.id == ctx.lift(id))

  override def toId(t: Person): PersonId = t.id

}
