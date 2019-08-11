package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

private class CompositeKeyCrudMacro(val c: MacroContext) {

  import c.universe._

  def all(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run($dSchema)
      }
    """

  def createOrUpdate(entity: Tree)(dSchema: Tree): Tree =
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

  def createOrUpdateAndRead(entity: Tree)(dSchema: Tree): Tree =
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

  def create(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
            $dSchema.insertValue($entity)
          )
          $entity.id
       }
    """

  def createAndRead(entity: Tree)(dSchema: Tree): Tree =
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

  def update(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      val id = $entity.id
      util.Try {
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q.updateValue($entity))
      }
    """

  def updateAndRead(entity: Tree)(dSchema: Tree): Tree =
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

  def read(id: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q)
        .headOption
      }
    """

  def delete(id: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        val q = $dSchema.filter(_.id.fk1 == lift(id.fk1)).filter(_.id.fk2 == lift(id.fk2))
        run(q.delete) > 0
      }
    """

  def deleteByFilter(filter: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).delete
        ) > 0
      }
    """

  def searchByFilter(filter: Tree)(offset: Tree, limit: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).drop(lift($offset)).take(lift($limit))
        )
      }
    """

  def count(filter: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter($filter).size
        )
      }
    """
}
