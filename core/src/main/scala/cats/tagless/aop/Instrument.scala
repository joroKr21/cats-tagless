/*
 * Copyright 2019 cats-tagless maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.tagless.aop

import cats.tagless.FunctorK
import cats.tagless.derived.DerivedInstrument

import scala.annotation.implicitNotFound

/** The result of an algebra method `F[A]` instrumented with the method name. */
final case class Instrumentation[F[_], A](value: F[A], algebraName: String, methodName: String)

/** Type class for instrumenting an algebra. Note: This feature is experimental, API is likely to change.
  *
  * @tparam Alg
  *   The algebra to be instrumented.
  */
@implicitNotFound("Could not find an instance of Instrument for ${Alg}")
trait Instrument[Alg[_[_]]] extends FunctorK[Alg] {
  def instrument[F[_]](af: Alg[F]): Alg[Instrumentation[F, *]]
}

object Instrument extends DerivedInstrument {

  // =======================
  // Generated by simulacrum
  // =======================

  @inline def apply[Alg[_[_]]](implicit instance: Instrument[Alg]): Instrument[Alg] = instance

  trait AllOps[Alg[_[_]], F[_]] extends Ops[Alg, F] with FunctorK.AllOps[Alg, F] {
    type TypeClassType <: Instrument[Alg]
    val typeClassInstance: TypeClassType
  }

  object ops {
    implicit def toAllInstrumentOps[Alg[_[_]], F[_]](target: Alg[F])(implicit tc: Instrument[Alg]): AllOps[Alg, F] {
      type TypeClassType = Instrument[Alg]
    } = new AllOps[Alg, F] {
      type TypeClassType = Instrument[Alg]
      val self = target
      val typeClassInstance: TypeClassType = tc
    }
  }

  trait Ops[Alg[_[_]], F[_]] {
    type TypeClassType <: Instrument[Alg]
    val typeClassInstance: TypeClassType
    def self: Alg[F]
    def instrument: Alg[Instrumentation[F, *]] =
      typeClassInstance.instrument[F](self)
  }

  trait ToInstrumentOps {
    implicit def toInstrumentOps[Alg[_[_]], F[_]](target: Alg[F])(implicit tc: Instrument[Alg]): Ops[Alg, F] {
      type TypeClassType = Instrument[Alg]
    } = new Ops[Alg, F] {
      type TypeClassType = Instrument[Alg]
      val self = target
      val typeClassInstance: TypeClassType = tc
    }
  }

  object nonInheritedOps extends ToInstrumentOps
}
