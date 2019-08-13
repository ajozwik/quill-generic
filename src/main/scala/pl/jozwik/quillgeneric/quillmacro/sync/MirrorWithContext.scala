package pl.jozwik.quillgeneric.quillmacro.sync

import io.getquill.idiom.Idiom
import io.getquill.{ MirrorContext, NamingStrategy }
import pl.jozwik.quillgeneric.quillmacro.WithId
import pl.jozwik.quillgeneric.quillmacro.quotes.DateQuotes

import scala.language.experimental.macros

class MirrorContextDateQuotes[D <: Idiom, N <: NamingStrategy](idiom: D, naming: N)
  extends MirrorContext[D, N](idiom, naming)
  with MirrorWithContext
  with DateQuotes

trait MirrorWithContext {
  this: MirrorContext[_, _] =>
  type dQuery[T] = this.DynamicEntityQuery[T]

  def all[T](implicit dSchema: dQuery[T]): QueryMirror[T] = macro CrudMacro.all[T]

  def create[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.create[K, T]

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.createAndGenerateId[K, T]

  def createWithGenerateIdAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): T = macro CrudMacro.createWithGenerateIdAndRead[K, T]

  def createOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.createOrUpdate[K, T]

  def createOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): T = macro CrudMacro.createOrUpdateAndRead[K, T]

  def createAndGenerateIdOrUpdate[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): K = macro CrudMacro.createAndGenerateIdOrUpdate[K, T]

  def createWithGenerateIdOrUpdateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): T =
    macro CrudMacro.createWithGenerateIdOrUpdateAndRead[K, T]

  def update[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): ActionMirror = macro CrudMacro.update[K, T]

  def updateAndRead[K, T <: WithId[K]](entity: T)(implicit dSchema: dQuery[T]): T = macro CrudMacro.updateAndRead[K, T]

  def read[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): QueryMirror[T] = macro CrudMacro.callFilterOnIdTree[K]

  def delete[K, T <: WithId[K]](id: K)(implicit dSchema: dQuery[T]): ActionMirror = macro CrudMacro.delete[K]

  def deleteByFilter[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Long = macro CrudMacro.deleteByFilter

  def searchByFilter[T](filter: T => Boolean)(offset: Int, limit: Int)(implicit dSchema: dQuery[T]): QueryMirror[T] = macro CrudMacro.searchByFilter[T]

  def count[T](filter: T => Boolean)(implicit dSchema: dQuery[T]): Long = macro CrudMacro.count
}
