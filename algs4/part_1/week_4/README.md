整个 puzzle-8 的问题的实现，其实在课程的 specification 中已经表述得很清楚了，这里我记录几点我在实现过程中比较困扰的点

#### 构造 SearchNode

怎么用 Board 这个类表达 parentNode，记录 moves、priority，这是我一开始做的时候怎么搞都不明白的地方，specification 里提供的 Board API 也没
提供啊 ？？其实只要自己构造一个 SearchNode 类包装一下 Board，把要存储的字段放进去就好了，当时真是蠢得可以，其实还是 JS 写多了，缺乏面向对象的编程
能力。之后的作业就知道了，自己要去构造必要的 Inner Class

#### 有解性问题

这个问题也是一开始完全想不明白：
1.  为什么交换任意两点能改变 8-puzzle 问题的有解性？
2.  这个特性该怎么用？

#### 其他

其他的问题，比如要使用 StringBuilder，而不是 String 字符串，使用内建的 Stack/Queue 等等，这些代码提交后系统会给出反馈，不过相信对于 Java
基础扎实的同学，这些都不是问题。还有，很多实现里提到了为了节省内存，用`char[]`代替`int[][]`，这里我倒是没遇到，没有深究。

下面我们回到有解性问题

关于如何利用交换任意两点能够改变 8-puzzle 问题的有解性，其实 specification 里讲得已经很清楚了，交替 (原文是 in lockstep) 对
initialBoard/twinBoard 做 minPQ 的 delMin/insert 操作。主要问题在于，怎么证明这个特性。这里我简单地记录一下我的想法

关于 slider puzzle 是否有解，其实是和 inversions(逆序数) 相关。对于 n 阶 slider puzzle，其是否有解，取决于其逆序数，以及一些其他条件
* 推论1:	对于基础的三阶 slider puzzle，若逆序数是奇数，则 slider puzzle 无解；若逆序数是偶数，则 slider puzzle 有解。
* 推论2:	对于能用 N 阶问题的有解性，参照以下条件:
  * 1.	If N is odd, then puzzle instance is solvable if number of inversions is even in the input state.
  * 2.	If N is even, puzzle instance is solvable if
		    the blank is on an even row counting from the bottom (second-last, fourth-last, etc.) and number of inversions is odd.
		    the blank is on an odd row counting from the bottom (last, third-last, fifth-last, etc.) and number of inversions is even.
  * 3.	For all other cases, the puzzle instance is not solvable.

推论 1 参见: https://www.geeksforgeeks.org/check-instance-8-puzzle-solvable/

推论 2 参见: https://www.geeksforgeeks.org/check-instance-15-puzzle-solvable/

同时推荐阅读 https://blog.csdn.net/Jason_Ranger/article/details/52613393。

那么，对于三阶问题，通过上述推论 1，和课程 specification 里 Detecting unsolvable boards 的提示，我们不难发现，对于有解的三阶问题，
其 twin() 一定不可解，对于无解的三阶问题，其 twin() 问题一定可解。

下面的过程是说明了**交换任意数对可以改变逆序数对的奇偶性**，至于逆序数对的奇偶性对于问题有解性的影响，上面提供的材料中有一部分说明，我也只是看懂了而已。。。
并不会证明 = =||

若交换两数为 a、b，实际排列为 …, a, …, b, … ，a 之前的叫做 pre_a 段，a、b 之间的叫做 a_b 段，b 之后的叫做 after_b 段。交换 a、b 后:
1.	pre_a 段和 after_b 段，无论相对 a 还是 b，逆序对数都不会改变，实际变化的是 a_b 段所产生的逆序对数。设 K 为 a_b 段的数字个数
2.	记 inv_a、inv_b 为 a_b 段数字所分别对应 a、b 的逆序对数；记 inv_a_swap、inv_b_swap 为交换后 b_a 段数字所分别对应 b、a 的逆序对数
3.	
    * 若 a < b，则
    ```
    inv_a_swap = larger_than_a
    inv_b_swap = less_than_b = less_than_a + between_a_b
    inv_a_swap + inv_b_swap = larger_than_a + less_than_a + between_a_b = K + between_a_b
    inv_a = less_than_a = less_than_b - between_a_b
    inv_b = larger_than_b
    inv_a + inv_b = less_than_b - between_a_b + larger_than_b = K - between_a_b
    所以交换 a、b 所产生的逆序对数增量，是 2 * between_a_b 以及交换 a、b 本身产生的那对逆序对，总共是 2 * between_a_b + 1，改变逆序数的奇偶性
    ```
    
	  * a > b 同理
4.	综上，交换 a、b 后，会改变逆序对数的奇偶性，结合推论 1，即有了三阶问题是否有解的方法

对于四阶及高阶问题，个人感觉总体思路差不多，根据推论 2，可能除了奇偶性，应该还有别的特征，没有深究

