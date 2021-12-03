/*
 * Copyright (C) 2021-present, Chenai Nakam(chenai.nakam@gmail.com)
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

package hobby.chenai.nakam.txdsl.core

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 03/12/2021
  */
package object coin {
  type Coin     = AbsCoinGroup
  type Cash     = AbsCashGroup
  type Token    = AbsTokenGroup
  type CoinUnt  = AbsCoinGroup#Unt
  type CashUnt  = AbsCashGroup#Unt
  type TokenUnt = AbsTokenGroup#Unt
  type CoinAmt  = AbsCoinGroup#AbsCoin
  type CashAmt  = AbsCashGroup#AbsCoin
  type TokenAmt = AbsTokenGroup#AbsCoin

  lazy val ZERO: BigDecimal = 0
  lazy val ONE: BigDecimal  = 1
  lazy val TEN: BigDecimal  = 10

  lazy val ZEROi: BigInt = 0
  lazy val ONEi: BigInt  = 1
  lazy val TENi: BigInt  = 10
}
