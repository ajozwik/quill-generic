package pl.jozwik.quillgeneric.quillmacro

import scala.reflect.macros.whitebox.{ Context => MacroContext }

class QuillMacro(val c: MacroContext) {

  import c.universe._

  def all[T](t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(quote {
        query[$t]
      })
    """

  def insertOrUpdate[T](entity: Tree, filter: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      if (run(${c.prefix}.quote {
        ${c.prefix}.query[$t].filter($filter).update(lift($entity))
      }) == 0) {
          run(quote {
            query[$t].insert(lift($entity))
          })
      }
      ()
    """

  def create[T](entity: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
          run(quote {
            query[$t].insert(lift($entity))
          })
    """

  def merge[T](entity: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(${c.prefix}.quote {
        ${c.prefix}.query[$t].update(lift($entity))
      })
    """

  def mergeByFilter[T](filter: Tree, action: Tree, actions: Tree*)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(${c.prefix}.quote {
        ${c.prefix}.query[$t].filter($filter).update(action, actions:_*)
      })
    """

  def deleteByFilter[T](filter: Tree)(implicit t: WeakTypeTag[T]): Tree =
    q"""
      import ${c.prefix}._
      run(${c.prefix}.quote {
        ${c.prefix}.query[$t].filter($filter).delete
      })
    """
}