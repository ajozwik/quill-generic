package pl.jozwik.quillgeneric.async

import io.getquill.{ MysqlAsyncContext, SnakeCase }
import pl.jozwik.quillgeneric.model.{ Person, PersonId }
import pl.jozwik.quillgeneric.quillmacro.QueriesAsync

import scala.concurrent.{ ExecutionContext, Future }

class PersonAsyncRepository extends AsyncRepository[PersonId, Person] {
  private val ctx = new MysqlAsyncContext(SnakeCase, "async.mysql") with QueriesAsync

  import ctx._

  def all(implicit ex: ExecutionContext): Future[Seq[Person]] =
    ctx.all[Person]

  def create(person: Person)(implicit ex: ExecutionContext): Future[PersonId] =
    ctx.create[PersonId, Person](person)

  def createOrUpdate(entity: Person)(implicit ex: ExecutionContext): Future[PersonId] =
    ctx.createOrUpdate[PersonId, Person](entity)

  def read(id: PersonId)(implicit ex: ExecutionContext): Future[Seq[Person]] =
    ctx.run(query[Person].filter(_.id == lift(id)))

  def update(person: Person)(implicit ex: ExecutionContext): Future[Long] =
    ctx.merge[Person](person)

  def update(id: PersonId, action: Person => (Any, Any), actions: Function[Person, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long] =
    ctx.mergeById[Person](_.id == lift(id), action, actions: _*)

  def delete(id: PersonId)(implicit ex: ExecutionContext): Future[Boolean] =
    ctx.deleteByFilter[Person](_.id == lift(id))
}
