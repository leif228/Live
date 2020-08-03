package com.wj.work.widget.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

import com.wj.work.R;
import com.wj.work.widget.adapter.ShoppingAdapter;
import com.wj.work.widget.entity.Product;
import com.google.common.collect.Lists;
import com.lib.kit.utils.LL;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物袋
 * <p>
 * 2020/5/14 9:27
 * 物界
 * {www.wj.com
 *
 * @author Ly
 */
public class ShoppingPackageDialog extends Dialog {

    private Context context;
    private RecyclerView recyclerView;
    private ShoppingAdapter mAdapter;

    private EditText etSearch;

    public ShoppingPackageDialog(Context context,List<Product> data) {
        super(context, R.style.bottom_dialog);
        this.context = context;
        this.originalData= Lists.newArrayList();
        this.originalData.addAll(data);
    }

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_shopping_package, null);
        view.findViewById(R.id.btnSearch).setOnClickListener(onclick);
        view.findViewById(R.id.lmbReturn).setOnClickListener(onclick);
        initSearchPanel(view);
        setContentView(view);
        initRecyclerView(view);
        setCancelable(true);//
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        if (window == null) return;
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    private void initSearchPanel(View rootView) {
        etSearch = rootView.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    searchResetData("");
                } else {
                    searchResetData(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private List<Product> originalData;

    /**
     * 根据搜索内容  重置数据
     */
    private void searchResetData(String searchMsg) {
        LL.V("searchResetData -- searchMsg="+searchMsg);

        if (null == originalData || originalData.size() <= 0) {
            return;
        }
        if (TextUtils.isEmpty(searchMsg)) {
            mAdapter.getData().clear();
            LL.V("searchMsg -- "+originalData.size());
            mAdapter.getData().addAll(originalData);
            mAdapter.notifyDataSetChanged();
            return;
        }
        List<Product> afterSearch = new ArrayList<>();
        for (Product item : originalData) {
            if (isIncludeForSearch(searchMsg, item)) {
                afterSearch.add(item);
            }
        }
        mAdapter.getData().clear();
        mAdapter.getData().addAll(afterSearch);
        mAdapter.notifyDataSetChanged();
    }

    private boolean isIncludeForSearch(String searchMsg, Product item) {
        String describe = item.getDescribe();
        String name = item.getName();
        if (!TextUtils.isEmpty(describe) && describe.contains(searchMsg)) return true;
        if (!TextUtils.isEmpty(name) && name.contains(searchMsg)) return true;
        return false;
    }

    private void initRecyclerView(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        List<Product> adapterData=Lists.newArrayList();
        adapterData.addAll(originalData);
        mAdapter = new ShoppingAdapter(adapterData);
        recyclerView.setAdapter(mAdapter);
    }

    private View.OnClickListener onclick = v -> {
        switch (v.getId()) {
            case R.id.lmbReturn:
                dismiss();
                break;
            case R.id.btnSearch:
                attemptDoSearch();
                break;
        }
    };

    private void attemptDoSearch() {
        String key = etSearch.getText().toString();
        searchResetData(key);
    }

    private Consumer<Integer> consumer;

    public ShoppingPackageDialog subscribe(Consumer<Integer> consumer) {
        this.consumer = consumer;
        return this;
    }

}
