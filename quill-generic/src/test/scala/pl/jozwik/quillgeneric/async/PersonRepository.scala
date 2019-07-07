package pl.jozwik.quillgeneric.async

import io.getquill.{ MysqlAsyncContext, SnakeCase }
import pl.jozwik.quillgeneric.model.Person

import scala.concurrent.{ ExecutionContext, Future }

class PersonRepository {
  private val ctx = new MysqlAsyncContext(SnakeCase, "async.mysql") with QueriesAsync

  import ctx._

  def create(person: Person)(implicit ex: ExecutionContext): Future[Long] =
    ctx.create[Person](person)

  def read(id: Int)(implicit ex: ExecutionContext): Future[Seq[Person]] =
    ctx.run(query[Person].filter(_.id == lift(id)))

  def update(person: Person)(implicit ex: ExecutionContext): Future[Long] =
    ctx.merge[Person](person)

  def update(id: Int, action: Person => (Any, Any), actions: Function[Person, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long] =
    ctx.mergeById[Person](_.id == lift(id), action, actions: _*)

  def delete(id: Int)(implicit ex: ExecutionContext): Future[Long] =
    ctx.deleteByFilter[Person](_.id == lift(id))
}
