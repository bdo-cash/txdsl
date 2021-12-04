/*
 * Copyright (C) 2017-present, Chenai Nakam(chenai.nakam@gmail.com)
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

import hobby.chenai.nakam.txdsl.core.coin.Coin

/**
  * @author Chenai Nakam(chenai.nakam@gmail.com)
  * @version 1.0, 25/05/2017
  */
class Fee[+GT <: Coin, +GF <: Coin](val coinGroup: GT, val feeGroup: GF) {
  import feeGroup._

  /** 假定手续费只收一种货币。如果需要多个，可以创建多个本对象。
    *
    * @param quota      固定费用。注意方法 `costs()` 的返回值的单位与本单位相同。
    * @param percentage 按比例收取的费用。
    * @param baseline   费用基准线。
    */
  class Rule(val quota: COIN, val percentage: BigDecimal, val baseline: COIN) {
    import quota.t2

    /** 计算交易费。
      *
      * @param amount 交易的数量
      * @param ex     交易所
      * @return 交易总费用。注意单位`unit`将与`baseline`相同。
      */
    def costs(amount: coinGroup.COIN)(implicit ex: AbsExchange): COIN = {
      // `amount`和`quota`可能是同一个币种，因此`to()`操作有可能不剪切精度(precision)，所以把精度独立计算。
      // 但鉴于实测结果为0的情况(1 BTC = 38580.95 USDT)，还是不根据精度进行四舍五入了。
      // [coinFfd]coin:0.0000175 BTC, result:0 BTC, ffd:4.
      baseline max (quota + (amount to (quota.unit, true)) * percentage)
    }
  }
}
