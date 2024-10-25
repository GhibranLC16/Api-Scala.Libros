package models



import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class BookDAO @Inject()(dbConfigProvider: DatabaseConfigProvider) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import profile.api._

  private val book = TableQuery[Books]

  def all(): Future[Seq[Book]] = db.run(book.result)

  def getById(id: Long): Future[Option[Book]] = db.run(book.filter(_.id === id).result.headOption)

  def create(newBook: Book): Future[Long] = {
    val insertQuery = (book returning book.map(_.id)) += newBook.copy(id = None)
    db.run(insertQuery).map {
      case Some(id) => id
      case None => throw new Exception("Failed to insert book, no ID returned")
    }
  }


  def update(id: Long, updatedBook: Book): Future[Int] = db.run(book.filter(_.id === id).update(updatedBook))

  def delete(id: Long): Future[Int] = db.run(book.filter(_.id === id).delete)
}