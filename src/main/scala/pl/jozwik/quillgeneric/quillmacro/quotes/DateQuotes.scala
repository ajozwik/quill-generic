package pl.jozwik.quillgeneric.quillmacro.quotes

import java.time.{ LocalDate, LocalDateTime }

import io.getquill.context.Context

trait DateQuotes {
  this: Context[_, _] =>

  implicit class LocalDateTimeQuotes(left: LocalDateTime) {
    def >(right: LocalDateTime) = quote(infix"$left > $right".as[Boolean])

    def <(right: LocalDateTime) = quote(infix"$left < $right".as[Boolean])

    def >=(right: LocalDateTime) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: LocalDateTime) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class LocalDateQuotes(left: LocalDate) {
    def >(right: LocalDate) = quote(infix"$left > $right".as[Boolean])

    def <(right: LocalDate) = quote(infix"$left < $right".as[Boolean])

    def >=(right: LocalDate) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: LocalDate) = quote(infix"$left <= $right".as[Boolean])
  }

}