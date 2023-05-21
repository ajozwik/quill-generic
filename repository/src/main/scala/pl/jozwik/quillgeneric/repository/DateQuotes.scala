package pl.jozwik.quillgeneric.repository

import io.getquill.context.Context

import java.time.{ Instant, LocalDate, LocalDateTime }

@SuppressWarnings(Array("org.wartremover.warts.PublicInference"))
trait DateQuotes {
  this: Context[_, _] =>

  // scalastyle:off
  implicit class LocalDateTimeQuotes(left: LocalDateTime) {
    def >(right: LocalDateTime) = quote(sql"$left > $right".as[Boolean])

    def <(right: LocalDateTime) = quote(sql"$left < $right".as[Boolean])

    def >=(right: LocalDateTime) = quote(sql"$left >= $right".as[Boolean])

    def <=(right: LocalDateTime) = quote(sql"$left <= $right".as[Boolean])
  }

  implicit class LocalDateTimeQuotesOption(left: Option[LocalDateTime]) {
    def >(right: Option[LocalDateTime]) = quote(sql"$left > $right".as[Boolean])

    def <(right: Option[LocalDateTime]) = quote(sql"$left < $right".as[Boolean])

    def >=(right: Option[LocalDateTime]) = quote(sql"$left >= $right".as[Boolean])

    def <=(right: Option[LocalDateTime]) = quote(sql"$left <= $right".as[Boolean])
  }

  implicit class LocalDateQuotes(left: LocalDate) {
    def >(right: LocalDate) = quote(sql"$left > $right".as[Boolean])

    def <(right: LocalDate) = quote(sql"$left < $right".as[Boolean])

    def >=(right: LocalDate) = quote(sql"$left >= $right".as[Boolean])

    def <=(right: LocalDate) = quote(sql"$left <= $right".as[Boolean])
  }

  implicit class LocalDateQuotesOption(left: Option[LocalDate]) {
    def >(right: Option[LocalDate]) = quote(sql"$left > $right".as[Boolean])

    def <(right: Option[LocalDate]) = quote(sql"$left < $right".as[Boolean])

    def >=(right: Option[LocalDate]) = quote(sql"$left >= $right".as[Boolean])

    def <=(right: Option[LocalDate]) = quote(sql"$left <= $right".as[Boolean])
  }

  implicit class InstantQuotes(left: Instant) {
    def >(right: Instant) = quote(sql"$left > $right".as[Boolean])

    def <(right: Instant) = quote(sql"$left < $right".as[Boolean])

    def >=(right: Instant) = quote(sql"$left >= $right".as[Boolean])

    def <=(right: Instant) = quote(sql"$left <= $right".as[Boolean])
  }

  implicit class InstantQuotesOption(left: Option[Instant]) {
    def >(right: Option[Instant]) = quote(sql"$left > $right".as[Boolean])

    def <(right: Option[Instant]) = quote(sql"$left < $right".as[Boolean])

    def >=(right: Option[Instant]) = quote(sql"$left >= $right".as[Boolean])

    def <=(right: Option[Instant]) = quote(sql"$left <= $right".as[Boolean])
  }
  // scalastyle:on
}
