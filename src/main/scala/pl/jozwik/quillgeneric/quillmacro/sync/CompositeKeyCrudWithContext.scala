package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.context.Context
import pl.jozwik.quillgeneric.quillmacro.{ CompositeKey, WithId }

import scala.language.experimental.macros
import scala.util.Try

trait CompositeKeyCrudWithContext {
  this: Context[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  def all[T](implicit dSchema: dQuery[T]): Try[Seq[T]] = macro CompositeKeyCrudMacro.all

  def create[K <: CompositeKey[_, _], T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro CompositeKeyCrudMacro.create[T]

  def createAndRead[K <: CompositeKey[_, _], T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CompositeKeyCrudMacro.createAndRead[T]

  def createOrUpdate[K <: CompositeKey[_, _], T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[K] = macro CompositeKeyCrudMacro.createOrUpdate[T]

  def createOrUpdateAndRead[K <: CompositeKey[_, _], T <: WithId[K]](
    entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CompositeKeyCrudMacro.createOrUpdateAndRead[T]

  def update[T](entity: T)(implicit dSchema: dQuery[T]): Try[Long] = macro CompositeKeyCrudMacro.update[T]

  def updateAndRead[T](entity: T)(implicit dSchema: dQuery[T]): Try[T] = macro CompositeKeyCrudMacro.updateAndRead[T]

  def read[K <: CompositeKey[_, _], T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Try[Option[T]] = macro CompositeKeyCrudMacro.read[K]

  def delete[K <: CompositeKey[_, _], T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): Try[Boolean] = macro CompositeKeyCrudMacro.delete

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Try[Boolean] = macro CompositeKeyCrudMacro.deleteByFilter

  def searchByFilter[T](filter: T => Boolean)(
    offset: Int,
    limit: Int)(implicit dSchema: dQuery[T]): Try[Seq[T]] = macro CompositeKeyCrudMacro.searchByFilter

  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Try[Long] = macro CompositeKeyCrudMacro.count
}
