### Tips

* virtual sites
* backwash
* bonus
* 使用 edu.princeton.cs.algs4 的 library

#### virtual sites

课程里提到了这种方法，添加两个 virtual sites。一个用来 union `row = 1` 的行的所有 open sites，另一个用来 union `row = N` 的行的所有 open sites。这样，我们就可以通过判断 `connected(topVirtual, bottomVirtual)` 的值来确定 percolate 与否，而不用循环遍历 `row1` 和 `rowN` 这两行，再去逐个比较 `connected(sites[row1, colI], sites[rowN, colI])` 了。Coursera 的课程上有很清晰的图示。

#### backwash

上面提到的 virtual sites 会存在一个问题：如果已经存在一条 percolate 的路径，即 `connected(topVirtual, bottomVirtual) == true`，这时再在 rowN 行 open 一个 site，那么会产生 backwash(倒灌，回流)。因为 open 行 rowN 上的 site 会 union `sites[rowN, colI]` 和 bottomVirtual，而 bottomVirutal 和 topVirtual 已经 connected，所以 `sites[rowN, colI]` 会和 topVirtual connected，造成 `isFull(rowN, colI)` 返回 true，而实际上它是通过 bottomVirtual 连通传递到了 topVirtual，任何与 `sites[rowN, colI]` union 之后的点都会有同样的问题。backwash 的图示可见 `backwash.jpg`，该图出处源自 [知乎](https://zhuanlan.zhihu.com/p/34922195)

要验证是否有 backwash 的问题，可以用 `input4.txt` 的数据，按照顺序 open 7 个 sites，最后验证 `sites[4,  4]`，代码如下：

```
Percolation p = new Percolation(4);
p.open(4, 1);
p.open(3, 1);
p.open(2, 1);
p.open(1, 1);
p.open(1, 4);
p.open(2, 4);
p.open(4, 4);
System.out.println(p.isFull(4, 4));
```

#### bonus

这是习题评判的一个附加题，总内存的消耗量，在 report 中有这样一个 case：

>Test 2 (bonus): check that total memory <= 11 n^2 + 128 n + 1024 bytes
>   -  failed memory test for n = 64
>==> FAILED

#### edu.princeton.cs.algs4 library

algs4.jar 里包含了所有涉及算法及功能函数，在 PercolationStats.java 里计算均值和标准差时，评判会要求优先使用 StdStats 的内置方法
