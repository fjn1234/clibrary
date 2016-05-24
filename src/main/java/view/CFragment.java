package view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hugh.clibrary.R;

import java.util.ArrayList;
import java.util.HashMap;

import entities.NotifyUpdateEntity;
import obj.CustomAttrs;
import utils.SystemUtil;
import utils.ViewUtil;

public class CFragment extends Fragment {

    protected static final String RESULT_CANCEL = "result_cancel";
    protected static final String RESULT_OK = "result_ok";
    public static final int ACTIVITY_ROOT_ID = 0x7f080000;
    public enum Result {RESULT_OK, RESULT_CANCEL}

    private int contentId = -1;
    protected View contentView = null;
    private ArrayList<EditText> editViews = new ArrayList<>();
    private Animation fragmentStartAnim, fragmentEndAnim;
    private int animEnterCome = 0, animEnterGo = 0, animExitCome = 0, animExitGo = 0;
    protected static boolean isStartingFragment = false;
    private Class resultClass;
    private String resultTag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (contentId > 0)
            contentView = LayoutInflater.from(getActivity()).inflate(contentId, null);
        if (fragmentStartAnim != null)
            contentView.startAnimation(fragmentStartAnim);
        addNotifyUpdate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sendNotifyUpdate(this.getClass(), NOTIFY_RESUME);
        isStartingFragment = false;
    }

    public void setFragmentAnim(Animation fragmentStartAnim, Animation fragmentEndAnim) {
        this.fragmentStartAnim = fragmentStartAnim;
        this.fragmentEndAnim = fragmentEndAnim;
    }

    public void startFragmentSingleTask(Fragment fragment) {
        if (getFragmentManager() == null) return;
        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
        startFragementAndFinish(fragment);
    }

    public void startFragement(final Fragment fragment) {
        isStartingFragment = true;
        closeSoftInput();
        if (getFragmentManager() == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        if (animEnterCome > 0 || animEnterGo > 0 || animExitCome > 0 || animExitGo > 0)
//            transaction.setCustomAnimations(animEnterCome, animEnterGo, animExitCome, animExitGo);
        transaction.replace(ACTIVITY_ROOT_ID + 0, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void startFragementAndFinish(Fragment fragment) {
        isStartingFragment = true;
        closeSoftInput();
        if (getFragmentManager() == null) return;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        if (animEnterCome > 0 || animEnterGo > 0 || animExitCome > 0 || animExitGo > 0)
//            transaction.setCustomAnimations(animEnterCome, animEnterGo, animExitCome, animExitGo);
        transaction.setCustomAnimations(0, 0, 0, 0);
        transaction.replace(ACTIVITY_ROOT_ID + 0, fragment).commitAllowingStateLoss();
    }

    public void startSingleTopActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void finish() {
        if (isStartingFragment) return;
        closeSoftInput();
        over();
    }

    public void over() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            isStartingFragment = true;
            getFragmentManager().popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }

    private OnRequestPermissionsResultListener onRequestPermissionsResultListener;

    public interface OnRequestPermissionsResultListener {
        void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }

    public void checkRequestPermissions(String[] permissions, int requestCode, OnRequestPermissionsResultListener requestPermissionsResultListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                requestPermissions(permissions, requestCode);
                this.onRequestPermissionsResultListener = requestPermissionsResultListener;
            }
        } else {
            int[] grantResults = SystemUtil.getGrantResults(getActivity(), permissions);
            if (requestPermissionsResultListener != null)
                requestPermissionsResultListener.requestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (onRequestPermissionsResultListener != null)
            onRequestPermissionsResultListener.requestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setContentView(int layoutResID) {
        this.contentId = layoutResID;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public View getContentView() {
        return contentView;
    }

    public int getContentId() {
        return contentId;
    }

    protected View findViewById(int id) {
        View view = contentView.findViewById(id);
        ViewUtil.loadCustomAttrs(view);
        if (view instanceof EditText)
            editViews.add((EditText) view);
        return view;
    }

    protected void loadCustomAttrs(int id) {
        ViewUtil.loadCustomAttrs(contentView.findViewById(id));
    }


    public void closeSoftInput() {
        if (getActivity() == null) return;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        for (EditText et : editViews) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

    public void addAutoCloseEditText(EditText et) {
        if (et == null) return;
        editViews.add(et);
    }

    private static ArrayList<CFragment> fragmentFlow;

    public static void startFragmentFlow() {
        fragmentFlow = new ArrayList<>();
    }

    public static void putFragmentToFlow(CFragment fragment) {
        if (fragmentFlow == null) return;
        fragmentFlow.add(fragment);
    }

    public static void overFragmentFlow() {
        if (fragmentFlow == null) return;
        CFragment fragment;
        for (int i = fragmentFlow.size() - 1; i >= 0; i--) {
            fragment = fragmentFlow.get(i);
            fragment.over();
        }
        fragmentFlow.clear();
        fragmentFlow = null;
    }

    public void setStartFragmentAnim(int animEnterCome, int animEnterGo, int animExitCome, int animExitGo) {
        this.animEnterCome = animEnterCome;
        this.animEnterGo = animEnterGo;
        this.animExitCome = animExitCome;
        this.animExitGo = animExitGo;
    }

    public void enableDefaultAnim() {
        int[] arr = getDefaultAnim();
        animEnterCome = arr[0];
        animEnterGo = arr[1];
        animExitCome = arr[2];
        animExitGo = arr[3];
    }

    private int[] getDefaultAnim() {
        int screenWidth = CustomAttrs.getScreenWidth();
        if (screenWidth == 0) return new int[]{0, 0, 0, 0};
        if (screenWidth >= 1440)
            return new int[]{R.anim.enter_come_1440, R.anim.enter_go_1440, R.anim.exit_come_1440, R.anim.exit_go_1440};
        if (screenWidth == 1080 || (screenWidth > 1080 && screenWidth < 1440))
            return new int[]{R.anim.enter_come_1080, R.anim.enter_go_1080, R.anim.exit_come_1080, R.anim.exit_go_1080};
        if (CustomAttrs.getScreenWidth() == 768 || (screenWidth > 768 && screenWidth < 1080))
            return new int[]{R.anim.enter_come_768, R.anim.enter_go_768, R.anim.exit_come_768, R.anim.exit_go_768};
        if (CustomAttrs.getScreenWidth() == 720 || (screenWidth > 720 && screenWidth < 768))
            return new int[]{R.anim.enter_come_720, R.anim.enter_go_720, R.anim.exit_come_720, R.anim.exit_go_720};
        return new int[]{0, 0, 0, 0};
    }

    public static boolean isStartingFragment() {
        return isStartingFragment;
    }

    public void setResultNotify(Class resultClass,String resultTag) {
        this.resultClass = resultClass;
        this.resultTag = resultTag;
    }

    //---------------------------------------------------------------------------------------
    public final static String NOTIFY_CREATE = "notify_create";
    public final static String NOTIFY_RESUME = "notify_resume";
    private Handler notifyUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onNotifyUpdate((NotifyUpdateEntity) msg.obj);
        }
    };

    protected void onNotifyUpdate(NotifyUpdateEntity notifyUpdateEntity) {
    }

    private static HashMap<String, HashMap<Integer, Handler>> notifyUpdateMap = new HashMap<>(20, 10);

    public static <Fgm extends CFragment> void sendNotifyUpdate(Class<Fgm> fgmClass, String notifyTag, Object entity) {
        sendNotifyUpdate(fgmClass, notifyTag, entity, 0);
    }

    public static <Fgm extends CFragment> void sendNotifyUpdate(Class<Fgm> fgmClass, String notifyTag) {
        sendNotifyUpdate(fgmClass, notifyTag, null, 0);
    }

    public static <Fgm extends CFragment> void sendNotifyUpdate(Class<Fgm> fgmClass, String notifyTag, long delay) {
        sendNotifyUpdate(fgmClass, notifyTag, null, delay);
    }

    public static <Fgm extends CFragment> void sendNotifyUpdate(Class<Fgm> fgmClass, String notifyTag, Object entity, long delay) {
        String tag = fgmClass.getName();
        if (!notifyUpdateMap.containsKey(tag)) return;
        NotifyUpdateEntity notifyUpdateEntity = new NotifyUpdateEntity(notifyTag, entity);
        HashMap<Integer, Handler> handlerList = notifyUpdateMap.get(tag);
        Handler handler;
        Message message;
        for (int i : handlerList.keySet()) {
            handler = handlerList.get(i);
            message = handler.obtainMessage();
            message.obj = notifyUpdateEntity;
            handler.sendMessageDelayed(message, delay);
        }
    }

    public void sendNotifyResult(Object entity) {
        sendNotifyUpdate(resultClass, resultTag, entity);
    }

    public void sendNotifyResult(String resultTag, Object entity) {
        sendNotifyUpdate(resultClass, resultTag, entity);
    }

    private void addNotifyUpdate() {
        String tag = this.getClass().getName();
        HashMap<Integer, Handler> handlerList;
        if (notifyUpdateMap.containsKey(tag)) {
            handlerList = notifyUpdateMap.get(tag);
            if (!handlerList.containsKey(notifyUpdateHandler.hashCode()))
                handlerList.put(notifyUpdateHandler.hashCode(), notifyUpdateHandler);
        } else {
            handlerList = new HashMap<>(2, 2);
            handlerList.put(notifyUpdateHandler.hashCode(), notifyUpdateHandler);
            notifyUpdateMap.put(tag, handlerList);
        }
    }

    private void removeNotifyUpdate() {
        String tag = this.getClass().getName();
        if (!notifyUpdateMap.containsKey(tag)) return;
        HashMap<Integer, Handler> handlerList = notifyUpdateMap.get(tag);
        handlerList.remove(notifyUpdateHandler.hashCode());
        if (handlerList.size() == 0)
            notifyUpdateMap.remove(tag);
    }

    @Override
    public void onDestroy() {
        removeNotifyUpdate();
        super.onDestroy();
    }
}
