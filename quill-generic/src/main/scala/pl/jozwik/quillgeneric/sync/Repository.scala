package pl.jozwik.quillgeneric.sync

trait Repository[K, T] {
  def all: Seq[T]
  def create(entity: T): Long
  def read(id: Int): Seq[T]
  def update(T: T): Long
  def update(id: Int, action: T => (Any, Any), actions: Function[T, (Any, Any)]*): Long
  def delete(id: Int): Long
}
