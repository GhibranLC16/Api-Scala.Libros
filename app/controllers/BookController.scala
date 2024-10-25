package controllers

import javax.inject._
import models.Book
import play.api.libs.json._
import play.api.mvc._
import repositories.BookRepository
import scala.concurrent.{ExecutionContext, Future}

// Controlador para manejar las solicitudes HTTP relacionadas con los libros
@Singleton
class BookController @Inject()(cc: ControllerComponents, bookRepository: BookRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Obtener todos los libros
  def getAllBooks: Action[AnyContent] = Action.async {
    bookRepository.all().map { books =>
      Ok(Json.toJson(books))
    }
  }

  // Obtener un libro por su ID
  def getBookById(id: Long): Action[AnyContent] = Action.async {
    bookRepository.getById(id).map {
      case Some(book) => Ok(Json.toJson(book))
      case None => NotFound(Json.obj("error" -> s"Book with ID $id not found"))
    }
  }

  // Crear un nuevo libro
  def createBook: Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Book].fold(
      errors => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(errors)))),
      book => {
        bookRepository.create(book).map { createdBookId =>
          Created(Json.obj("id" -> createdBookId, "book" -> Json.toJson(book.copy(id = Some(createdBookId)))))
        }
      }
    )
  }


  // Actualizar un libro existente
  def updateBook(id: Long): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Book].fold(
      errors => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(errors)))),
      book => {
        bookRepository.update(id, book).map {
          case 0 => NotFound(Json.obj("error" -> s"Book with ID $id not found"))
          case _ => Ok(Json.toJson(book))
        }
      }
    )
  }

  // Eliminar un libro por su ID
  def deleteBook(id: Long): Action[AnyContent] = Action.async {
    println(s"Intentando eliminar libro con ID: $id") // Debugging
    bookRepository.delete(id).map {
      case 0 =>
        println(s"No se encontró el libro con ID: $id") // Debugging
        NotFound(Json.obj("error" -> s"Book with ID $id not found"))
      case _ =>
        println(s"Libro con ID $id eliminado") // Debugging
        NoContent // 204 No Content
    }
  }

}
