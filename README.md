# quill-generic support
Library of generic CRUD operation for quill library

[![Build Status](https://travis-ci.org/ajozwik/quill-generic.svg?branch=master)](https://travis-ci.org/ajozwik/quill-generic)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/macro-quill_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/macro-quill_2.12)

Example usage in:

 - [PersonRepository](/src/test/scala/pl/jozwik/quillgeneric/sync/repository/PersonRepository.scala) and [MyPersonRepository](src/test/scala/pl/jozwik/quillgeneric/sync/repository/MyPersonRepository.scala) (example of usage macro method like searchByFilter, count)
 - [PersonAsyncRepository](/src/test/scala/pl/jozwik/quillgeneric/async/PersonAsyncRepository.scala)
 
 Repositories are generated automatically by [sbt-quill-crud-generic](https://github.com/ajozwik/sbt-quill-crud-generic), see
[quill-macro-example](https://github.com/ajozwik/quill-macro-example)
