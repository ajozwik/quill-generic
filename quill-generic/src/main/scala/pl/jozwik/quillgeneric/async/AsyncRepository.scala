package pl.jozwik.quillgeneric.async

import scala.concurrent.{ ExecutionContext, Future }

trait AsyncRepository[K, T] {
  def all(implicit ex: ExecutionContext): Future[Seq[T]]
  def create(entity: T)(implicit ex: ExecutionContext): Future[Long]
  def read(id: Int)(implicit ex: ExecutionContext): Future[Seq[T]]
  def update(T: T)(implicit ex: ExecutionContext): Future[Long]
  def update(id: Int, action: T => (Any, Any), actions: Function[T, (Any, Any)]*)(implicit ex: ExecutionContext): Future[Long]
  def delete(id: Int)(implicit ex: ExecutionContext): Future[Long]
}
