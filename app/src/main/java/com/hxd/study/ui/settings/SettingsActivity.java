package com.hxd.study.ui.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.hxd.study.R;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

public class SettingsActivity extends AppCompatActivity {

    private QMUITopBarLayout mTopBar;
    private QMUIGroupListView mGroupListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        initTopBar();
        initGroupListView();
    }

    private void initViews() {
        mTopBar = findViewById(R.id.topbar);
        mGroupListView = findViewById(R.id.groupListView);
    }

    private void initTopBar() {
        mTopBar.addLeftImageButton(R.drawable.qmui_icon_topbar_back, R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                onBackPressed();
            }
        });
        mTopBar.setTitle(this.getClass().getName());
    }

    private void initGroupListView() {
        /*
         * 第一个Item
         */
        QMUICommonListItemView normalItem = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.drawable.googleg_standard_color_18),
                "第一个Item",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE
        );
        /*
         * 第二个Item
         */
        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.drawable.googleg_disabled_color_18),
                "第二个Item",
                "这是有详情的Item",
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE
        );
        /*
         * 第三个Item
         */
        QMUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.drawable.googleg_standard_color_18),
                "第三个Item",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetailBelow.setOrientation(QMUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");
        /*
         * 第四个Item
         */
        QMUICommonListItemView itemWithChevron = mGroupListView.createItemView("第四个Item");
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        /*
         * 第五个Item
         */
        QMUICommonListItemView itemWithSwitch = mGroupListView.createItemView("第五个Item");
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SettingsActivity.this, "checked = " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        /*
         * 第六个Item
         */
        QMUICommonListItemView itemWithCustom = mGroupListView.createItemView("第六个Item");
        itemWithCustom.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        itemWithCustom.showRedDot(true);
        itemWithCustom.setRedDotPosition(QMUICommonListItemView.REDDOT_POSITION_LEFT);
        QMUILoadingView loadingView = new QMUILoadingView(SettingsActivity.this);
        itemWithCustom.addAccessoryCustomView(loadingView);
        /*
         * 第七个Item
         */
        @SuppressLint("InlinedApi")
        QMUICommonListItemView itemWithDate = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.drawable.googleg_standard_color_18),
                "第七个Item",
                null,
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.AUTOFILL_TYPE_LIST
        );
        itemWithDate.showRedDot(true,true);
        itemWithDate.setRedDotPosition(QMUICommonListItemView.REDDOT_POSITION_LEFT);
        /*
         * 第八个Item
         */
        @SuppressLint("InlinedApi")
        QMUICommonListItemView itemWithToggle = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.drawable.googleg_standard_color_18),
                "第八个Item",
                "来个相关描述吧",
                QMUICommonListItemView.VERTICAL,
                QMUICommonListItemView.AUTOFILL_TYPE_TOGGLE
        );
        itemWithToggle.setDetailText("重新修改描述");
        itemWithToggle.showNewTip(true);
        itemWithToggle.showRedDot(true, true);
        itemWithToggle.setRedDotPosition(QMUICommonListItemView.REDDOT_POSITION_RIGHT);

        /*
         * 点击事件
         */
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(SettingsActivity.this, text + " is Clicked", Toast.LENGTH_SHORT).show();
                    if (((QMUICommonListItemView) v).getAccessoryType() == QMUICommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((QMUICommonListItemView) v).getSwitch().toggle();
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(this, 20);
        QMUIGroupListView.newSection(this)
                .setTitle("Section 1:默认提供的样式")
                .setDescription("Section 1 的相关描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(normalItem, onClickListener)
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithDetailBelow, onClickListener)
                .addItemView(itemWithChevron, onClickListener)
                .addItemView(itemWithSwitch, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(this)
                .setTitle("Section 2: 自定义右侧 View")
                .addItemView(itemWithCustom, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(this)
                .setTitle("Section 3: 带日期的自定义 View")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithDate, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(this)
                .setUseDefaultTitleIfNone(true)
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithToggle, onClickListener)
                .addTo(mGroupListView);

    }

}
