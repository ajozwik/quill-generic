# quill-generic support
Library of generic CRUD operation for quill library. Only dynamic queries are supported.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/84d50129877e41068bc0eeb4943b825d)](https://app.codacy.com/manual/ajozwik/quill-generic?utm_source=github.com&utm_medium=referral&utm_content=ajozwik/quill-generic&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.com/ajozwik/quill-generic.svg?branch=master)](https://travis-ci.com/ajozwik/quill-generic)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ajozwik/macro-quill_2.12.svg?label=latest%20release%20for%202.12)](http://search.maven.org/#search|ga|1|g%3A%22com.github.ajozwik%22%20AND%20a%3A%22macro-quill_2.12%22)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ajozwik/macro-quill_2.13.svg?label=latest%20release%20for%202.13)](http://search.maven.org/#search|ga|1|g%3A%22com.github.ajozwik%22%20AND%20a%3A%22macro-quill_2.13%22)
[![Coverage Status](https://coveralls.io/repos/github/ajozwik/quill-generic/badge.svg?branch=master)](https://coveralls.io/github/ajozwik/quill-generic?branch=master)

Always when you start new project you must to write a lot of boilerplate code for handling simple CRUD operations. Purpose of this library is to support creating CRUD repository with [Quill](https://github.com/getquill/quill) library.

Purpose of CRUD operations - [Repository](/macro-quill/src/main/scala/pl/jozwik/quillgeneric/quillmacro/Repository.scala) - where F is monad like scala.util.Try/monix.eval.Task/scala.concurrent.Future :
```scala
package pl.jozwik.quillgeneric.quillmacro

import scala.language.higherKinds

trait RepositoryWithGeneratedId[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T, generatedId: Boolean = true): F[K]

  def createAndRead(entity: T, generatedId: Boolean = true): F[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true): F[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): F[T]
}

trait RepositoryCompositeKey[K <: CompositeKey[_, _], T <: WithId[K]] extends Repository[K, T]

trait Repository[K, T <: WithId[K]] extends BaseRepository[K, T] {
  def create(entity: T): F[K]

  def createAndRead(entity: T): F[T]

  def createOrUpdate(entity: T): F[K]

  def createOrUpdateAndRead(entity: T): F[T]
}

trait BaseRepository[K, T <: WithId[K]] extends WithMonad {

  def all: F[Seq[T]]

  def read(id: K): F[Option[T]]

  def readUnsafe(id: K): F[T]

  def update(t: T): F[Long]

  def updateAndRead(t: T): F[T]

  def delete(id: K): F[Long]

}

trait WithMonad {
  type F[_]
}
```

Because macro is created in compile time - we need to know primary key. Case class for database entity has to have field id - the primary key [WithId](/macro-quill/src/main/scala/pl/jozwik/quillgeneric/quillmacro/WithId.scala)
If you have composite key - it has to extends io.getquill.Embedded and one of traits [CompositeKey](/macro-quill/src/main/scala/pl/jozwik/quillgeneric/quillmacro/CompositeKey.scala)

General [Repository and RepositoryCompositeKey](/macro-quill/src/main/scala/pl/jozwik/quillgeneric/quillmacro/Repository.scala) is designed for manual handling of primary key. If database generate for you key - use [RepositoryWithGeneratedId](/macro-quill/src/main/scala/pl/jozwik/quillgeneric/quillmacro/Repository.scala)

Current we support Try:

 - [PersonRepository](/quill-jdbc-macro/src/test/scala/pl/jozwik/quillgeneric/sync/jdbc/repository/PersonRepository.scala) and [MyPersonRepository](/quill-jdbc-macro/src/test/scala/pl/jozwik/quillgeneric/sync/jdbc/repository/MyPersonRepository.scala) (example of usage macro method like searchByFilter, count)
 
And monix Task:
 
 - [PersonCustomRepositoryJdbc](/quill-jdbc-monix-macro/src/test/scala/pl/jozwik/quillgeneric/monix/repository/PersonCustomRepositoryJdbc.scala)
 
Synchronized and monix repositories are generated automatically by [sbt-quill-crud-generic](https://github.com/ajozwik/sbt-quill-crud-generic), see build.sbt in
[quill-macro-example](https://github.com/ajozwik/quill-macro-example)
