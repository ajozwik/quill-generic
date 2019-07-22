package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.language.experimental.macros
import scala.util.Try

trait Queries {
  this: Context[_, _] =>
  def all[T](implicit dSchema: this.DynamicEntityQuery[T]): Try[Seq[T]] = macro QuillCrudMacro.all

  def createOrUpdate[K, T <: WithId[K]](
    entity: T,
    generateId: Boolean)(implicit dSchema: this.DynamicEntityQuery[T]): Try[K] = macro QuillCrudMacro.createOrUpdate

  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: this.DynamicEntityQuery[T]): Try[K] = macro QuillCrudMacro.create

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: this.DynamicEntityQuery[T]): Try[K] = macro QuillCrudMacro.createAndGenerateId

  def update[T](entity: T)(implicit dSchema: this.DynamicEntityQuery[T]): Try[Long] = macro QuillCrudMacro.update

  def read[K, T <: WithId[K]](id: K)(implicit dSchema: this.DynamicEntityQuery[T]): Try[Option[T]] = macro QuillCrudMacro.read

  def updateByFilter[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*)(implicit dSchema: this.DynamicEntityQuery[T]): Try[Long] = macro QuillCrudMacro.mergeByFilter

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: this.DynamicEntityQuery[T]): Try[Boolean] = macro QuillCrudMacro.deleteByFilter
}
