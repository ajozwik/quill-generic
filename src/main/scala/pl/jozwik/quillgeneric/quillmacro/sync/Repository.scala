package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.util.Try

trait Repository[K, T <: WithId[K]] {

  def all: Try[Seq[T]]

  def create(entity: T, generateId: Boolean = false): Try[K]

  def createOrUpdate(entity: T, generateId: Boolean = false): Try[K]

  def read(id: K): Try[Option[T]]

  def update(t: T): Try[Long]

  def update(id: K, action: T => (Any, Any), actions: Function[T, (Any, Any)]*): Try[Long]

  def delete(id: K): Try[Boolean]

}
