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

package cats.tagless
package tests.simple

import cats.Id
import cats.arrow.FunctionK
import cats.tagless.syntax.all.*
import cats.tagless.tests.experimental

@experimental
class FunctorKSpec extends munit.FunSuite with Fixtures {

  test("DeriveMacro should derive instance for a simple algebra") {
    val functorK = Derive.functorK[SimpleService]
    assert(functorK.isInstanceOf[FunctorK[SimpleService]])
  }

  test("FunctorK should be a valid instance for a simple algebra") {
    val functorK = Derive.functorK[SimpleService]
    val optionalInstance = functorK.mapK(instance)(FunctionK.liftFunction[Id, Option](Option.apply))

    assertEquals(optionalInstance.id(), Some(instance.id()))
    assertEquals(optionalInstance.list(0), Some(instance.list(0)))
    assertEquals(optionalInstance.lists(0, 1), Some(instance.lists(0, 1)))
    assertEquals(optionalInstance.paranthesless, Some(instance.paranthesless))
    assertEquals(optionalInstance.tuple, Some(instance.tuple))
  }

  test("DeriveMacro should derive instance for a not simple algebra") {
    assert(compileErrors("Derive.functorK[NotSimpleService]").isEmpty)
  }

  test("FunctorK derives syntax") {
    val optionalInstance = instance.mapK(FunctionK.liftFunction[Id, Option](Option.apply))

    assertEquals(optionalInstance.id(), Some(instance.id()))
    assertEquals(optionalInstance.list(0), Some(instance.list(0)))
    assertEquals(optionalInstance.lists(0, 1), Some(instance.lists(0, 1)))
    assertEquals(optionalInstance.paranthesless, Some(instance.paranthesless))
    assertEquals(optionalInstance.tuple, Some(instance.tuple))
  }
}
