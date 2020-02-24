## 编程题回顾

### 暴力算法

主要踩的坑在 key 的问题，原因是我对 RedBlackBST 的 put 操作理解有误，对于相同的 key，RedBlackBST 的 put 操作会更新 其 value，而不是新增，因此 key 必须是 Point2D，不能是 double 类型的 x/y 坐标，不然 key 很容易重复，导致红黑树无法包含所有的点集信息。

### Kd Tree 算法

## 哈希算法

主要的问题，如何解决 collision (哈希碰撞)

#### separate chaining (散列链接)

对于 collision 的 key，存放一个 LinkedList，搜索时先计算 key 的 hash，找到对应 value 的 LinkedList，然后遍历该 LinkedList

#### linear probing (线性探针)

先计算 hash，然后检测该 hash 是否为空，若空，则直接插入，若非空，则检测下一个 index 是否为空，如此循环，直至数组所有 index 都被写满，即没有 index 为空。

linear probing 的效率极大取决于数组的大小，以及所有 key 的数量，即 size。因为很显然，key/array_size 的比例越大，越有可能出现 index 被占用的情况，可能需要探测多个 index 才能找到空的位置

