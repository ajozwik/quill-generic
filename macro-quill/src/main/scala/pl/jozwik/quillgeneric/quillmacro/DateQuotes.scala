package pl.jozwik.quillgeneric.quillmacro

import java.time.{ Instant, LocalDate, LocalDateTime }
import java.util.Date

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

  implicit class InstantQuotes(left: Instant) {
    def >(right: Instant) = quote(infix"$left > $right".as[Boolean])

    def <(right: Instant) = quote(infix"$left < $right".as[Boolean])

    def >=(right: Instant) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: Instant) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit val instantEncoder: MappedEncoding[Date, Instant] = MappedEncoding[Date, Instant](_.toInstant)
  implicit val instantDecoder: MappedEncoding[Instant, Date] = MappedEncoding[Instant, Date](Date.from)

}
