package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

private class CrudMacro(val c: MacroContext) {

  import c.universe._

  def all(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run($dSchema)
      }
    """

  def createAndGenerateIdOrUpdate[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
          val q = $dSchema.filter(_.id == lift(id))
          val result = run(
            q.updateValue($entity)
          )
          if (result == 0) {
            run($dSchema.insertValue($entity).returningGenerated(_.id))
          } else {
            $entity.id
          }
        }
      }
    """

  def createWithGenerateIdOrUpdateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
          val q = $dSchema.filter(_.id == lift(id))
          val result = run(
            q.updateValue($entity)
          )
          val newId =
            if (result == 0) {
              run($dSchema.insertValue($entity).returningGenerated(_.id))
            } else {
              $entity.id
            }
          run($dSchema.filter(_.id == lift(newId)))
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$newId"))
        }
      }
    """

  def createOrUpdate[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
         val q = $dSchema.filter(_.id == lift(id))
         val result = run(
             q.updateValue($entity)
          )
          if(result == 0){
            run($dSchema.insertValue($entity))
          }
        }
        $entity.id
      }
    """

  def createOrUpdateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
         val q = $dSchema.filter(_.id == lift(id))
         val result = run(
             q.updateValue($entity)
          )
          if(result == 0){
            run($dSchema.insertValue($entity))
          }
          run(q)
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$id"))
        }
      }
    """

  def create[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
            $dSchema.insertValue($entity)
          )
          $entity.id
       }
    """

  def createAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        transaction {
          run($dSchema.insertValue($entity))
          val q = $dSchema.filter(_.id == lift(id))
          run(q)
          .headOption
          .getOrElse(throw new NoSuchElementException(s"$$id"))
        }
       }
    """

  def createAndGenerateId[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run($dSchema.insertValue($entity).returningGenerated(_.id))
       }
    """

  def createWithGenerateIdAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
       transaction {
         val id = run($dSchema.insertValue($entity).returningGenerated(_.id))
         val q = $dSchema.filter(_.id == lift(id))
         run(q)
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$id"))
        }
       }
    """

  def update[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        val q = $dSchema.filter(_.id == lift(id))
        run(q.updateValue($entity))
      }
    """

  def updateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
        val q = $dSchema.filter(_.id == lift(id))
        run(q.updateValue($entity))
        run(q)
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$id"))
       }
      }
    """

  def read[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $dSchema.filter(_.id == lift($id))
        run(q)
        .headOption
      }
    """

  def delete[K: c.WeakTypeTag](id: c.Expr[K])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $dSchema.filter(_.id == lift($id))
        run(
           q.delete
        ) > 0
      }
    """

  def deleteByFilter(filter: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).delete
        ) > 0
      }
    """

  def searchByFilter(filter: Tree)(offset: Tree, limit: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
        )
      }
    """

  def count(filter: Tree)(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).size
        )
      }
    """
}
