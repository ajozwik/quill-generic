package pl.jozwik.quillgeneric.quillmacro

import java.time.{ Instant, LocalDate, LocalDateTime }
import java.util.Date

import io.getquill.context.Context

@SuppressWarnings(Array("org.wartremover.warts.PublicInference"))
trait DateQuotes {
  this: Context[_, _] =>

  implicit val instantEncoder: MappedEncoding[Date, Instant] = MappedEncoding[Date, Instant](_.toInstant)
  implicit val instantDecoder: MappedEncoding[Instant, Date] = MappedEncoding[Instant, Date](Date.from)

  //scalastyle:off
  implicit class LocalDateTimeQuotes(left: LocalDateTime) {
    def >(right: LocalDateTime) = quote(infix"$left > $right".as[Boolean])

    def <(right: LocalDateTime) = quote(infix"$left < $right".as[Boolean])

    def >=(right: LocalDateTime) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: LocalDateTime) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class LocalDateTimeQuotesOption(left: Option[LocalDateTime]) {
    def >(right: Option[LocalDateTime]) = quote(infix"$left > $right".as[Boolean])

    def <(right: Option[LocalDateTime]) = quote(infix"$left < $right".as[Boolean])

    def >=(right: Option[LocalDateTime]) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: Option[LocalDateTime]) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class LocalDateQuotes(left: LocalDate) {
    def >(right: LocalDate) = quote(infix"$left > $right".as[Boolean])

    def <(right: LocalDate) = quote(infix"$left < $right".as[Boolean])

    def >=(right: LocalDate) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: LocalDate) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class LocalDateQuotesOption(left: Option[LocalDate]) {
    def >(right: Option[LocalDate]) = quote(infix"$left > $right".as[Boolean])

    def <(right: Option[LocalDate]) = quote(infix"$left < $right".as[Boolean])

    def >=(right: Option[LocalDate]) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: Option[LocalDate]) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class InstantQuotes(left: Instant) {
    def >(right: Instant) = quote(infix"$left > $right".as[Boolean])

    def <(right: Instant) = quote(infix"$left < $right".as[Boolean])

    def >=(right: Instant) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: Instant) = quote(infix"$left <= $right".as[Boolean])
  }

  implicit class InstantQuotesOption(left: Option[Instant]) {
    def >(right: Option[Instant]) = quote(infix"$left > $right".as[Boolean])

    def <(right: Option[Instant]) = quote(infix"$left < $right".as[Boolean])

    def >=(right: Option[Instant]) = quote(infix"$left >= $right".as[Boolean])

    def <=(right: Option[Instant]) = quote(infix"$left <= $right".as[Boolean])
  }
  //scalastyle:on
}
