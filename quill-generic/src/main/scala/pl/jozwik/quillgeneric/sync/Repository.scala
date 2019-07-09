package pl.jozwik.quillgeneric.sync

import scala.util.Try

trait Repository[K, T] {
  def all: Try[Seq[T]]

  def create(entity: T, generateId: Boolean = false): Try[K]

  def createOrUpdate(entity: T): Try[K]

  def read(id: K): Try[Option[T]]

  def update(T: T): Try[Long]

  def update(id: K, action: T => (Any, Any), actions: Function[T, (Any, Any)]*): Try[Long]

  def delete(id: K): Try[Boolean]

}
