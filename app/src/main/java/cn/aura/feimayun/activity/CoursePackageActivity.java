package cn.aura.feimayun.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.aura.feimayun.R;
import cn.aura.feimayun.adapter.CoursePackageActivity_ListView1_Adapter;
import cn.aura.feimayun.application.MyApplication;
import cn.aura.feimayun.util.RequestURL;
import cn.aura.feimayun.util.Util;
import cn.aura.feimayun.view.ProgressDialog;

public class CoursePackageActivity extends BaseActivity implements View.OnClickListener {
    //请求面授详情
    private static Handler handleNetwork;
    private ProgressDialog progressDialog;
    private TextView activity_course_package_textview1;
    private ImageView activity_course_package_imageview1;
    private TextView activity_course_package_textview2;
    private TextView activity_course_package_textview3;
    private TextView activity_course_package_textview4;
    private LinearLayout activity_course_package_linearLayout;
    //存放intent中获取到的数据
    private String data_id;
    private String data_teach_type;
    private CoursePackageActivity_ListView1_Adapter adapter;

    @SuppressLint("HandlerLeak")
    private void handler() {
        handleNetwork = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj.toString().equals("网络异常")) {
                    Toast.makeText(CoursePackageActivity.this, "请检查网络连接_Error25", Toast.LENGTH_LONG).show();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } else {
                    parseJSON(msg.obj.toString());
                }
            }
        };
    }

    private void parseJSON(String s) {
        try {
            JSONTokener jsonTokener = new JSONTokener(s);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            int status = jsonObject.getInt("status");
            if (status == 1) {//解析成功
                //解析data
                Map<String, String> dataMap = new HashMap<>();
                JSONObject dataObject = jsonObject.getJSONObject("data");
                dataMap.put("id", dataObject.getString("id"));
                dataMap.put("data_id", dataObject.getString("data_id"));
                dataMap.put("series_1", dataObject.getString("series_1"));
                dataMap.put("series_2", dataObject.getString("series_2"));
                dataMap.put("title", dataObject.getString("title"));
                dataMap.put("number", dataObject.getString("number"));
                dataMap.put("rprice", dataObject.getString("rprice"));
                dataMap.put("price", dataObject.getString("price"));
                dataMap.put("tea_id", dataObject.getString("tea_id"));
                dataMap.put("is_sell", dataObject.getString("is_sell"));
                dataMap.put("is_rec", dataObject.getString("is_rec"));
                dataMap.put("rec_time", dataObject.getString("rec_time"));
                dataMap.put("hours", dataObject.getString("hours"));
                dataMap.put("plays", dataObject.getString("plays"));
                dataMap.put("browse", dataObject.getString("browse"));
                dataMap.put("sells", dataObject.getString("sells"));
                dataMap.put("bg_url", dataObject.getString("bg_url"));
                dataMap.put("start_time", dataObject.getString("start_time"));
                dataMap.put("end_time", dataObject.getString("end_time"));
                dataMap.put("liveTotal", dataObject.getString("liveTotal"));
                dataMap.put("name", dataObject.getString("name"));
                dataMap.put("videoTotal", dataObject.getString("videoTotal"));
                dataMap.put("tikuTotal", dataObject.getString("tikuTotal"));
                dataMap.put("dyTotal", dataObject.getString("dyTotal"));
                dataMap.put("expire", dataObject.getString("expire"));
                dataMap.put("mediaTotal", dataObject.getString("mediaTotal"));
                dataMap.put("videoHours", dataObject.getString("videoHours"));
                dataMap.put("paperTotal", dataObject.getString("paperTotal"));
                dataMap.put("qaTotal", dataObject.getString("qaTotal"));
                dataMap.put("about", dataObject.getString("about"));
                dataMap.put("isBuy", dataObject.getString("isBuy"));

                //解析catalogue
                List<Map<String, String>> catalogueList = new ArrayList<>();
                JSONArray catalogueArray = jsonObject.getJSONArray("catalogue");
                for (int i = 0; i < catalogueArray.length(); i++) {
                    JSONObject catalogueObject = catalogueArray.getJSONObject(i);
                    Map<String, String> catalogueMap = new HashMap<>();
                    catalogueMap.put("id", catalogueObject.getString("id"));
                    catalogueMap.put("company_id", catalogueObject.getString("company_id"));
                    catalogueMap.put("lid", catalogueObject.getString("lid"));
                    catalogueMap.put("pid", catalogueObject.getString("pid"));
                    catalogueMap.put("type", catalogueObject.getString("type"));
                    catalogueMap.put("name", catalogueObject.getString("name"));
                    catalogueMap.put("tid", catalogueObject.getString("tid"));
                    catalogueMap.put("sort", catalogueObject.getString("sort"));
                    catalogueMap.put("create_uid", catalogueObject.getString("create_uid"));
                    catalogueMap.put("create_time", catalogueObject.getString("create_time"));
                    catalogueMap.put("update_time", catalogueObject.getString("update_time"));
                    catalogueMap.put("update_uid", catalogueObject.getString("update_uid"));
                    catalogueMap.put("status", catalogueObject.getString("status"));
                    catalogueMap.put("is_del", catalogueObject.getString("is_del"));
                    if (catalogueObject.has("children")) {
                        JSONArray childrenArray = catalogueObject.getJSONArray("children");
                        catalogueMap.put("children", childrenArray.toString());
                    }
                    catalogueList.add(catalogueMap);
                }

                //得到数据，开始初始化控件内容
                activity_course_package_textview1.setText(dataMap.get("name"));
                RequestOptions options = new RequestOptions().fitCenter();
                if (Util.isOnMainThread()) {
                    Glide.with(MyApplication.context).load(dataMap.get("bg_url")).apply(options).into(activity_course_package_imageview1);
                }
                activity_course_package_textview2.setText("课程视频共" + dataMap.get("mediaTotal") + "个");
                activity_course_package_textview3.setText("模拟题库共" + dataMap.get("paperTotal") + "套试卷");
                activity_course_package_textview4.setText("专项答疑共" + dataMap.get("dyTotal") + "条");

                adapter.setData(catalogueList, dataMap.get("id"), dataMap.get("series_1"), dataMap.get("series_2"));
                adapter.notifyDataSetChanged();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_package);

        if (MyApplication.APP_STATUS == MyApplication.APP_STATUS_NORMAL) {
            Intent intent = getIntent();
            data_id = intent.getStringExtra("data_id");
            data_teach_type = intent.getStringExtra("data_teach_type");
            handler();
            //网络请求、初始化数据
            initData();
            initView();
        }
    }

    //发送网络请求
    private void initData() {
        String uid = Util.getUid();
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", data_id);
        paramsMap.put("teach_type", data_teach_type);
        paramsMap.put("uid", uid);
        RequestURL.sendPOST("https://app.feimayun.com/Lesson/detail", handleNetwork, paramsMap, CoursePackageActivity.this);
    }

    private void initView() {
        //设置CutoutMode
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(params);
        }
        if (Build.VERSION.SDK_INT >= 28) {
            findViewById(R.id.root).setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets windowInsets) {
                    DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                    if (displayCutout != null) {
                        int left = displayCutout.getSafeInsetLeft();
                        int top = displayCutout.getSafeInsetTop();
                        int right = displayCutout.getSafeInsetRight();
                        int bottom = displayCutout.getSafeInsetBottom();
                        findViewById(R.id.view).getLayoutParams().height = top;
                    }
                    return windowInsets.consumeSystemWindowInsets();
                }
            });
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        //返回按钮布局
        RelativeLayout headtitle_layout = findViewById(R.id.headtitle_layout);
        headtitle_layout.setOnClickListener(this);
        //标题
        TextView headtitle_textview = findViewById(R.id.headtitle_textview);
        headtitle_textview.setText("课程详情");

        activity_course_package_textview1 = findViewById(R.id.activity_course_package_textview1);
        activity_course_package_imageview1 = findViewById(R.id.activity_course_package_imageview1);
        activity_course_package_textview2 = findViewById(R.id.activity_course_package_textview2);
        activity_course_package_textview3 = findViewById(R.id.activity_course_package_textview3);
        activity_course_package_textview4 = findViewById(R.id.activity_course_package_textview4);
        ListView activity_course_package_listview1 = findViewById(R.id.activity_course_package_listview1);

        adapter = new CoursePackageActivity_ListView1_Adapter(this);
        activity_course_package_listview1.setAdapter(adapter);
        activity_course_package_linearLayout = findViewById(R.id.activity_course_package_linearLayout);
        activity_course_package_linearLayout.setOnClickListener(v -> {
            String apud = Util.getUid();
            if (TextUtils.isEmpty(apud)) {
                Toast.makeText(this, "请登录~", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, MaiAuthen.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.headtitle_layout) {//左上角的返回按钮
            finish();
        }
    }
}
