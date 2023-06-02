# quill-generic support
Library of generic CRUD operation for quill library. Only dynamic queries are supported.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/84d50129877e41068bc0eeb4943b825d)](https://app.codacy.com/manual/ajozwik/quill-generic?utm_source=github.com&utm_medium=referral&utm_content=ajozwik/quill-generic&utm_campaign=Badge_Grade_Dashboard)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ajozwik/repository_2.12.svg?label=latest%20release%20for%202.12)](http://search.maven.org/#search|ga|1|g%3A%22com.github.ajozwik%22%20AND%20a%3A%22repository_2.12%22)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.ajozwik/repository_2.13.svg?label=latest%20release%20for%202.13)](http://search.maven.org/#search|ga|1|g%3A%22com.github.ajozwik%22%20AND%20a%3A%22repository_2.13%22)
[![Coverage Status](https://coveralls.io/repos/github/ajozwik/quill-generic/badge.svg?branch=master)](https://coveralls.io/github/ajozwik/quill-generic?branch=master)
[![Scala CI](https://github.com/ajozwik/quill-generic/actions/workflows/scala.yml/badge.svg)](https://github.com/ajozwik/quill-generic/actions/workflows/scala.yml)
[![codecov](https://codecov.io/gh/ajozwik/quill-generic/branch/master/graph/badge.svg?token=Fwyqkbx7Qf)](https://codecov.io/gh/ajozwik/quill-generic)

Always when you start new project you must to write a lot of boilerplate code for handling simple CRUD operations. Purpose of this library is to support creating CRUD repository with [Quill](https://github.com/getquill/quill) library.

Purpose of CRUD operations - [Repository](/repository/src/main/scala/pl/jozwik/quillgeneric/repository/Repository.scala) - where F is monad like scala.util.Try/monix.eval.Task/scala.concurrent.Future :
```scala
package pl.jozwik.quillgeneric.repository


trait WithTransaction[F[_]] {
  def inTransaction[A](task: F[A]): F[A]
}

trait RepositoryWithGeneratedId[F[_], K, T <: WithId[K], UP] extends BaseRepository[F, K, T, UP] {
  def create(entity: T, generatedId: Boolean = true): F[K]

  def createAndRead(entity: T, generatedId: Boolean = true): F[T]

  def createOrUpdate(entity: T, generatedId: Boolean = true): F[K]

  def createOrUpdateAndRead(entity: T, generatedId: Boolean = true): F[T]
}


trait Repository[F[_], K, T <: WithId[K], UP] extends BaseRepository[F, K, T, UP] {
  def create(entity: T): F[K]

  def createAndRead(entity: T): F[T]

  def createOrUpdate(entity: T): F[K]

  def createOrUpdateAndRead(entity: T): F[T]
}

trait BaseRepository[F[_], K, T <: WithId[K], UP] {

  def all: F[Seq[T]]

  def read(id: K): F[Option[T]]

  def readUnsafe(id: K): F[T]

  def update(t: T): F[UP]

  def updateAndRead(t: T): F[T]

  def delete(id: K): F[UP]

  def deleteAll: F[UP]

}

trait RepositoryWithTransaction[F[_], K, T <: WithId[K], UP] extends Repository[F, K, T, UP] with WithTransaction[F]

trait RepositoryWithTransactionWithGeneratedId[F[_], K, T <: WithId[K], UP] extends RepositoryWithGeneratedId[F, K, T, UP] with WithTransaction[F]

```

Because protoquill-macro's are created in compile time - we need to know primary key. Case class for database entity has to have field id - the primary key [WithId](/repository/src/main/scala/pl/jozwik/quillgeneric/repository/WithId.scala)
If you have composite key you need to create case class like [Cell4dId](/repository/src/main/scala/pl/jozwik/quillgeneric/model/Cell4dId.scala):

For table
```sql
CREATE TABLE IF NOT EXISTS CELL4D (
    `X`  INT NOT NULL,
    `Y`  INT NOT NULL,
    `Z`  INT NOT NULL,
    `T`  INT NOT NULL,
    `OCCUPIED` BOOLEAN,
    PRIMARY KEY (`X`, `Y`, `Z`, `T`)
)
```
Compose key can look like:

```scala
final case class Cell4dId(fk1: Int, fk2: Int, fk3: Int, fk4: Long) {
  def x: Int = fk1

  def y: Int = fk2

  def z: Int = fk3

  def t: Long = fk4

}
```

General [Repository](/repository/src/main/scala/pl/jozwik/quillgeneric/repository/Repository.scala) is designed for manual handling of primary key. If database generate for you key - use [RepositoryWithGeneratedId](/repository/src/main/scala/pl/jozwik/quillgeneric/repository/Repository.scala)

Current we support Try:

 - [PersonRepository](/repository-jdbc-monad/src/test/scala/pl/jozwik/quillgeneric/monad/repository/PersonRepository.scala) and [MyPersonRepository](/repository-jdbc-monad/src/test/scala/pl/jozwik/quillgeneric/monad/repository/MyPersonRepository.scala) 
 
And monix Task:
 
 - [PersonCustomRepositoryJdbc](/quill-jdbc-monix/src/test/scala/pl/jozwik/quillgeneric/monix/repository/PersonCustomRepositoryJdbc.scala)
 
Synchronized and monix repositories are generated automatically by [sbt-quill-crud-generic](https://github.com/ajozwik/sbt-quill-crud-generic), see build.sbt in
[quill-macro-example](https://github.com/ajozwik/quill-macro-example)
