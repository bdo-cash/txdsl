/*
 * Copyright (C) 2018-present, Chenai Nakam(chenai.nakam@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hobby.chenai.nakam.txdsl.core.exch

import hobby.chenai.nakam.lang.TypeBring
import hobby.chenai.nakam.txdsl.core.coin._

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 10/07/2018
  */
object coinTpeImpl {
  def apply[PriTC <: CoinAmt, PriCC <: CoinAmt]: coinTpeImpl[PriTC, PriCC] = new coinTpeImpl[PriTC, PriCC] {}
}

trait coinTpeImpl[PriTC <: CoinAmt, PriCC <: CoinAmt] {
  implicit lazy val ptc = new TypeBring[PriTC, PriTC, CoinAmt] {}.t2
  implicit lazy val pcc = new TypeBring[PriCC, PriCC, CoinAmt] {}.t2
}
