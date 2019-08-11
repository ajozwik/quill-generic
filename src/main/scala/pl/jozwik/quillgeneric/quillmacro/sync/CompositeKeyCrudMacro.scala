package pl.jozwik.quillgeneric.quillmacro.sync

import pl.jozwik.quillgeneric.quillmacro.CompositeKey

import scala.reflect.macros.whitebox.{ Context => MacroContext }

private class CompositeKeyCrudMacro(val c: MacroContext) {

  import c.universe._

  def all(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run($dSchema)
      }
    """

  def createOrUpdate[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction{
         val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
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
       transaction{
         val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
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
          val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
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
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q.updateValue($entity))
      }
    """

  def updateAndRead[T: c.WeakTypeTag](entity: c.Expr[T])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
       transaction {
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q.updateValue($entity))
        run(q)
         .headOption
         .getOrElse(throw new NoSuchElementException(s"$$id"))
       }
      }
    """

  def read[K: c.WeakTypeTag, T: c.WeakTypeTag](entity: c.Expr[K])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q)
        .headOption
      }
    """

  def delete(id: c.Expr[CompositeKey[_, _]])(dSchema: c.Expr[_]): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q.delete) > 0
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
