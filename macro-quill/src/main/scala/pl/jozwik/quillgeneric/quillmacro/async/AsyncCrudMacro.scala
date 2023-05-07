package pl.jozwik.quillgeneric.quillmacro.async

import scala.concurrent.ExecutionContext
import scala.reflect.macros.whitebox.{ Context => MacroContext }
@SuppressWarnings(Array("org.wartremover.warts.Any"))
class AsyncCrudMacro(val c: MacroContext) extends AbstractAsyncCrudMacro {

  import c.universe._

  def createAndGenerateIdOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        t <- if(result == 0){ run($dSchema.insertValue($entity).returningGenerated(_.id)) } else { concurrent.Future.successful(id)}
       } yield {
        t
       }
    """
  }

  def createWithGenerateIdOrUpdateAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        newId <- if(result == 0){ run($dSchema.insertValue($entity).returningGenerated(_.id)) } else { concurrent.Future.successful(id)}
        r <- run($dSchema.filter(_.id == lift(newId)))
       } yield {
        r.headOption.getOrElse(throw new NoSuchElementException(s"$$newId"))
       }
    """
  }

  def createWithGenerateIdAndRead[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      for {
         newId <- run($dSchema.insertValue($entity).returningGenerated(_.id))
         r <- run($dSchema.filter(_.id == lift(newId)))
       } yield {
         r.headOption.getOrElse(throw new NoSuchElementException(s"$$newId"))
      }
    """

  def createOrUpdate[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree = {
    val filter = callFilter[K, T](entity)(dSchema)
    q"""
      import ${c.prefix}._
      val id = $entity.id
      val q = $filter
      for {
        result <- run(q.updateValue($entity))
        t <- if(result == 0){ run($dSchema.insertValue($entity)) } else { concurrent.Future.successful(id) }
       } yield {
        id
       }
    """
  }



  def create[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: Tree)(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree =
    q"""
      import ${c.prefix}._
      for{
        _ <- run($dSchema.insertValue($entity))
      } yield {
        $entity.id
      }
    """





  def read[K: c.WeakTypeTag, T: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_], ex: c.Expr[ExecutionContext]): Tree = {
    val filter = callFilterOnId[K](id)(dSchema)
    q"""
      import ${c.prefix}._
      val q = $filter
      for {
        r <- run(q)
      } yield {
        r.headOption
      }
    """
  }



}
