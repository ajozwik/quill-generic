package pl.jozwik.quillgeneric.quillmacro.async

import io.getquill.context.async.AsyncContext
import pl.jozwik.quillgeneric.quillmacro.WithId

import scala.concurrent.{ ExecutionContext, Future }
import scala.language.experimental.macros

trait QueriesAsync {
  this: AsyncContext[_, _, _] =>
  def all[T](implicit
    tableName: String,
    ex: ExecutionContext): Future[List[T]] = macro QuillCrudAsyncMacro.all[T]

  def create[K, T <: WithId[K]](entity: T)(implicit
    tableName: String,
    ex: ExecutionContext): Future[K] = macro QuillCrudAsyncMacro.create[T]

  def createAndGenerateId[K, T <: WithId[K]](entity: T)(implicit
    tableName: String,
    ex: ExecutionContext): Future[K] = macro QuillCrudAsyncMacro.createAndGenerateId[T]

  def createOrUpdate[K, T <: WithId[K]](
    entity: T,
    generateId: Boolean)(implicit
    tableName: String,
    ex: ExecutionContext): Future[K] = macro QuillCrudAsyncMacro.createOrUpdate[T]

  def merge[T](entity: T)(implicit
    tableName: String,
    ex: ExecutionContext): Future[Long] = macro QuillCrudAsyncMacro.merge[T]

  def read[K, T <: WithId[K]](id: K)(implicit
    tableName: String,
    ex: ExecutionContext): Future[Option[T]] = macro QuillCrudAsyncMacro.read[T]

  def mergeById[T](
    filter: T => Boolean,
    action: Function[T, (Any, Any)],
    actions: Function[T, (Any, Any)]*)(implicit tableName: String, ex: ExecutionContext): Future[Long] = macro QuillCrudAsyncMacro.mergeByFilter[T]

  def deleteByFilter[T](filter: T => Boolean)(implicit
    tableName: String,
    ex: ExecutionContext): Future[Boolean] = macro QuillCrudAsyncMacro.deleteByFilter[T]
}
