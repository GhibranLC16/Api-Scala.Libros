package models

import play.api.libs.json._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

// Definición del modelo Book
case class Book(
                 id: Option[Long] = None,         // ID autoincremental
                 title: String,      // Título del libro
                 author: String,     // Autor del libro
                 pages: Int,         // Cantidad de páginas
                 price: Double       // Precio del libro
               )

// Companion object para JSON (de/serialización)
object Book {
  implicit val bookFormat: OFormat[Book] = Json.format[Book]  // Para manejar JSON
}

// Mapeo de la tabla "book"
class Books(tag: Tag) extends Table[Book](tag, "book") {
  def id: Rep[Option[Long]] = column[Long]("id", O.PrimaryKey, O.AutoInc).?
  def title: Rep[String] = column[String]("title")
  def author: Rep[String] = column[String]("author")
  def pages: Rep[Int] = column[Int]("pages")
  def price: Rep[Double] = column[Double]("price")

  // Método que mapea las columnas de la tabla a la case class Book
  def * : ProvenShape[Book] = (id, title, author, pages, price).mapTo[Book]
}
