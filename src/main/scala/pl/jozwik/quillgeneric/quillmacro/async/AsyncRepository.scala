package pl.jozwik.quillgeneric.quillmacro.async

import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.concurrent.{ ExecutionContext, Future }

trait AsyncRepository[K, T <: WithId[K]] {
  def all(implicit ex: ExecutionContext): Future[Seq[T]]

  def create(entity: T, generateId: Boolean = false)(implicit ex: ExecutionContext): Future[K]

  def createOrUpdate(entity: T, generateId: Boolean = false)(implicit ex: ExecutionContext): Future[K]

  def read(id: K)(implicit ex: ExecutionContext): Future[Seq[T]]

  def update(T: T)(implicit ex: ExecutionContext): Future[Long]

  def update(id: K, action: T => (Any, Any), actions: Function[T, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long]

  def delete(id: K)(implicit ex: ExecutionContext): Future[Boolean]
}
