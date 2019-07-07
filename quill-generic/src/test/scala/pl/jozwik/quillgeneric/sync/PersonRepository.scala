package pl.jozwik.quillgeneric.sync

import io.getquill.{ H2JdbcContext, SnakeCase }
import pl.jozwik.quillgeneric.model.Person

class PersonRepository(ctx: H2JdbcContext[SnakeCase.type] with Queries) extends Repository[Int, Person] {

  import ctx._

  override def all: Seq[Person] =
    ctx.all[Person]

  override def create(person: Person): Long =
    ctx.create[Person](person)

  override def read(id: Int): Seq[Person] =
    ctx.run(query[Person].filter(_.id == lift(id)))

  override def update(person: Person): Long =
    ctx.merge[Person](person)

  override def update(id: Int, action: Person => (Any, Any), actions: Function[Person, (Any, Any)]*): Long =
    ctx.mergeById[Person](_.id == lift(id), action, actions: _*)

  override def delete(id: Int): Long =
    ctx.deleteByFilter[Person](_.id == lift(id))
}
