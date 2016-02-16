package fs2.util

trait Monad[F[_]] extends Functor[F] {
  def map[A,B](a: F[A])(f: A => B): F[B] = bind(a)(f andThen (pure))
  def bind[A,B](a: F[A])(f: A => F[B]): F[B]
  def pure[A](a: A): F[A]

  // keeping this private for now
  private[fs2] def traverseVector[A,B](v: Vector[A])(f: A => F[B]): F[Vector[B]] =
    v.reverse.foldLeft(pure(Vector.empty[B])) {
      (tl,hd) => bind(f(hd)) { b => map(tl)(b +: _) }
    }
}
