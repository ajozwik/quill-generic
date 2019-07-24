package pl.jozwik.quillgeneric.quillmacro.sync

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillCrudMacro(val c: MacroContext) {

  import c.universe._

  def all(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
          $dSchema
        )
      }
    """

  def createOrUpdate(entity: Tree, generateId: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
       transaction{
          val result = run(
            $dSchema.filter(_.id == entity.id).updateValue($entity)
          )
          if (result == 0) {
              run(
                $dSchema.insertValue($entity).returning(_.id)
              )
          } else {
            $entity.id
          }
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

  def createAndGenerateId(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
          run(
           $dSchema.insertValue($entity).returningGenerated(_.id)
          )
       }
    """

  def update(entity: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
            $dSchema.updateValue($entity)
        )
      }
    """

  def read(id: Tree)(dSchema: Tree): Tree =
    q"""
      import ${c.prefix}._
      util.Try {
        run(
           $dSchema.filter(_.id == lift($id))
        ).headOption
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
}