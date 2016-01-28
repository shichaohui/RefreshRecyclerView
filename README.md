# RefreshRecyclerView
下拉刷新的RecyclerView，兼有上拉加载更多、添加头部、定制脚部功能，下拉刷新使用SwipeRefreshLayout。

效果图

![效果图](https://github.com/shichaohui/RefreshRecyclerView/blob/master/draw_sector_graph.gif)

## 用法

不想看我啰嗦的直接看[MainActivity.java](https://github.com/shichaohui/RefreshRecyclerView/blob/master/MainActivity.java)

### 添加Header和Footer

Footer有默认的，就是效果图上的那个，当然也可以使用以下方法定制Footer。
```
refreshView.setHeader(R.layout.header); // 添加布局作为Header
refreshView.setHeader(view); // 添加View作为Header

refreshView.setFooter(R.layout.footer); // 添加布局作为Footer
refreshView.setFooter(view); // 添加view作为Footer
```

### 刷新和加载更多

设置监听：

```
// 设置刷新监听
refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
          
        ... // 可以在这里执行数据的刷新
          
    }
});
// 设置加载更多监听
refreshView.setOnLoadMoreListener(new RefreshRecycleView.OnLoadMoreListener() {
    @Override
    public void onLoadMore() {
         
        ... // 可以在这里执行加载更多数据
         
    }
});
```

刷新完成后取消刷新动画：

```
refreshView.setRefreshing(false);
```

打开和关闭可加载更多状态：

```
refreshView.setLoadMoreEnable(false); // 不可再加载更多, 可以在没有更多数据时使用
refreshView.setLoadMoreEnable(true); // 打开加载更多
```

### 适配器

`RefreshRecyclerView.RefreshAdapter`是对Header、Footer等功能的支持，所以定制自己的适配器时要继承`RefreshRecyclerView.RefreshAdapter`，而不是继承`RecyclerView.Adapter`了。

`RefreshRecyclerView.RefreshAdapter`提供了两个抽象方法用来绑定视图和数据：

```
/**
 * 创建ViewHolder, 用来代替onCreateViewHolder()方法, 用法还是一样的
 *
 * @param parent   父控件
 * @param viewType 类型
 * @return ViewHolder的子类实例
 */
public abstract VH onCreateHolder(ViewGroup parent, int viewType);

/**
 * 给ViewHolder绑定数据, 用来代替onBindViewHolder(), 用法一样
 *
 * @param holder   ViewHolder的子类实例
 * @param position 位置
 */
public abstract void onBindHolder(VH holder, int position);
```

为了支持Header和Footer，`RefreshRecyclerView.RefreshAdapter`已重写`getItemViewType(int position)`方法，因此，为了不影响显示不同类型的子视图，`RefreshRecyclerView.RefreshAdapter`还提供了以下两个方法，有需要的话进行重写就可以了。

```
/**
 * 自定义获取子视图类型的方法
 *
 * @param position 位置
 * @return 类型
 */
public int getItemType(int position) {
    // 重写此方法，计算并返回自己的子视图类型
    return -1;
}

/**
 * 设置子视图类型, 如果有新的子视图类型, 直接往参数viewTypes中添加即可, 每个类型的值都要>3, 且不能重复
 *
 * @param viewTypes 子视图类型列表
 */
public void setItemTypes(List<Integer> viewTypes) {
    // 重写此方法，向viewTypes中添加自己的子视图类型，类型值必须大于3
}
```

具体的使用方法可以参考[MainActivity.java](https://github.com/shichaohui/RefreshRecyclerView/blob/master/MainActivity.java)

除了这些方法外就是RecyclerView的方法了，如`setLayoutManager(LayoutManager layout)`等。

如果以上方法不够用，比如要设置动画，我这里提供的类中并没有公开这个方法，要公开也很简单，参考setAdapter()方法的形式就可以了。

这里还有一个问题：就是`onBindHolder(VH holder, int position)`方法的position是减去了Header的数量的（不然无法跟数据集合中的position对应），所以在使用RecyclerView的需要以position为参数的方法时，需要把Header的数量加上（这里只能有1个Header，所以＋1就行了）。
