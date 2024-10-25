package repositories

import javax.inject.{Inject, Singleton}
import models.{Book, BookDAO}
import scala.concurrent.{ExecutionContext, Future}

// Repositorio para manejar lógica de negocio relacionada a los libros
@Singleton
class BookRepository @Inject()(bookDAO: BookDAO)(implicit ec: ExecutionContext) {

  // Obtener todos los libros
  def all(): Future[Seq[Book]] = bookDAO.all()

  // Obtener un libro por su ID
  def getById(id: Long): Future[Option[Book]] = bookDAO.getById(id)

  // Crear un nuevo libro
  def create(book: Book): Future[Long] = bookDAO.create(book)

  // Actualizar un libro existente
  def update(id: Long, book: Book): Future[Int] = bookDAO.update(id, book)

  // Eliminar un libro por su ID
  def delete(id: Long): Future[Int] = bookDAO.delete(id)
}
