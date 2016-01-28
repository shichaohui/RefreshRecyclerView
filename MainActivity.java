import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshRecycleView refreshView;
    private MyAdapter adapter;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshView = (RefreshRecycleView) findViewById(R.id.refresh_view);

        initData();

        refreshView.setLayoutManager(new LinearLayoutManager(this));
        refreshView.setHeader(R.layout.header);
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datas.add(0, "新数据");
                adapter.notifyDataSetChanged();
                // 停止刷新动画
                refreshView.setRefreshing(false);
            }
        });
        refreshView.setOnLoadMoreListener(new RefreshRecycleView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                datas.add("新加载数据");
                // 不可再加载更多, 可以在没有新数据时使用
                refreshView.setLoadMoreEnable(false);
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new MyAdapter(this, datas);
        refreshView.setAdapter(adapter);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        datas = new ArrayList<>();
        int flag = 1; // 标签
        for (int i = 1; i < 10; i++) {
            if (i % 4 == 1) { // 每四个数据加一个标签
                datas.add(flag + "");
                flag++;
            }
            datas.add("第" + i + "条");
        }
    }

    /**
     * 数据适配器, 继承RefreshRecycleView.RefreshAdapter
     */
    static class MyAdapter extends RefreshRecycleView.RefreshAdapter<MyViewHolder> {

        List<String> datas;
        // 数据的类型为 数据, 标签
        final int TYPE_DATA = 4, TYPE_FLAG = 5;

        /**
         * 创建适配器
         *
         * @param context
         * @param list    要显示的数据列表
         */
        public MyAdapter(Context context, List list) {
            super(context, list);
            datas = list;
        }

        @Override
        public MyViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            View view = super.inflater.inflate(R.layout.item_view, parent, false);
            if (viewType == TYPE_FLAG) {
                // 是标记就增加一个背景
                view.setBackgroundColor(Color.GRAY);
            }
            return new MyViewHolder(view);
        }

        @Override
        public void onBindHolder(MyViewHolder holder, int position) {
            holder.textTitle.setText(datas.get(position));
        }

        @Override
        public int getItemType(int position) {
            // 把数据转换成int, 抛异常的为数据, 其他为标记
            try {
                Integer.parseInt(datas.get(position));
                return TYPE_FLAG;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return TYPE_DATA;
            }
        }

        @Override
        public void setItemTypes(List<Integer> viewTypes) {
            // 添加数据类型
            viewTypes.add(TYPE_DATA);
            viewTypes.add(TYPE_FLAG);
        }
    }

    /**
     * 视图缓存
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
        }

    }

}
