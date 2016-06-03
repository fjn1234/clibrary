package uicontrols;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugh.clibrary.R;

import java.util.ArrayList;
import java.util.List;

import entities.PhotoEntity;
import interfaces.IView;
import obj.CRecyclerAdapter;
import obj.CellView;
import obj.CustomAttrs;
import view.CImageView;
import view.CRecyclerView;

/**
 * Created by Hugh on 2016/1/9.
 */
public class GalleryView extends CRecyclerView {

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GalleryView);
        maxLimit = ta.getInteger(R.styleable.GalleryView_cmaxLimit, -1);
        cellViewId = ta.getResourceId(R.styleable.GalleryView_ccellViewId, 0);
        itemPerRow = ta.getInteger(R.styleable.GalleryView_citemPerRow, 3);
        addMode = ta.getBoolean(R.styleable.GalleryView_caddMode, true);
        deleteAble = ta.getBoolean(R.styleable.GalleryView_cdeleteAble, false);
        int type = ta.getInt(R.styleable.GalleryView_curiType, 0);
        if (type == 0)
            uriType = UriType.Disk;
        else
            uriType = UriType.Url;
        mode = ta.getInteger(R.styleable.GalleryView_cmode, 3);
        if (mode == 1) {
            setLayoutManager(new GridLayoutManager(getContext(), itemPerRow));
        } else if (mode == 2) {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        ta.recycle();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryView(Context context) {
        this(context, null);
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public int getItemPerRow() {
        return itemPerRow;
    }

    //------------------------------------------------------------------
    private CRecyclerAdapter<String> adapter;
    private int cellViewId = -1;
    private int maxLimit = 10;
    private int itemPerRow = 1;
    private List<PhotoEntity> imgList;
    private UriType uriType = UriType.Disk;
    private int cellViewHeight = 0, mode;
    private boolean addMode, deleteAble;

    public enum UriType {
        Disk, Url
    }

    public void setUriType(UriType uriType) {
        this.uriType = uriType;
    }

    public void setCellViewLayout(int cellViewId) {
        this.cellViewId = cellViewId;
    }

    public void setImgList(List<PhotoEntity> imgList) {
        this.imgList = imgList;
    }

    public List<PhotoEntity> getImgList() {
        return imgList;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public int getCellViewHeight() {
        return cellViewHeight;
    }

    public void setAddMode(boolean addMode) {
        this.addMode = addMode;
    }

    public void setDeleteAble(boolean deleteAble) {
        this.deleteAble = deleteAble;
    }

    public void add(PhotoEntity entity) {
        if (imgList == null)
            imgList = new ArrayList<>();
        imgList.add(entity);
    }

    public void remove(PhotoEntity entity) {
        if (imgList == null) return;
        imgList.remove(entity);
    }

    public void remove(int position) {
        if (imgList == null) return;
        imgList.remove(position);
    }

    public void clear() {
        imgList = new ArrayList<>();
    }

    public int getImgeCount() {
        return imgList.size();
    }

    public PhotoEntity getPhotoEntity(int position) {
        return imgList.get(position);
    }

    public int size() {
        if (adapter == null) return 0;
        return adapter.size();
    }

    public void create() {
        if (cellViewId < 0) return;
        initAdapter();
        initData();
        setHeight();
    }

    public void setHeight() {
        int height = 0;
        switch (mode) {
            case 1:
                height = cellViewHeight * (int) Math.ceil(size() * 1f / getItemPerRow());
                break;
            case 2:
                height = cellViewHeight * size();
                break;
            case 3:
                height = cellViewHeight;
                break;
        }
        getCustomAttrs().setHeightRatio(height * 100f / CustomAttrs.getScreenHeight() + "%");
        loadCustomAttrs();
    }

    // 更新图片
    private void initData() {
        if (imgList == null)
            imgList = new ArrayList<>();
        int photoCount = imgList.size() + 1 > maxLimit ? maxLimit : imgList.size() + 1;
        if (!addMode)
            photoCount = imgList.size();
        for (int i = 0; i < photoCount; i++) {
            adapter.add(0, "");
        }
        adapter.notifyDataSetChanged();
        View v = LayoutInflater.from(getContext()).inflate(cellViewId, null).findViewById(R.id.iv_img);
        if (v instanceof IView.ICustomAttrs) {
            IView.ICustomAttrs iCustomAttrs = (IView.ICustomAttrs) v;
            iCustomAttrs.loadScreenArr();
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            cellViewHeight = iCustomAttrs.getCustomAttrs().getHeight() + lp.topMargin + lp.bottomMargin;
        }
    }

    private void initAdapter() {
        adapter = new CRecyclerAdapter<String>(getContext(), cellViewId) {

            @Override
            public void setData(final int position, CellView cell) {
                try {
                    final CImageView ivBg = (CImageView) cell.getView(R.id.iv_bg);
                    final CImageView ivImg = (CImageView) cell.getView(R.id.iv_img);
                    ivImg.setOnClickListener(null);
                    final PhotoEntity entity;
                    boolean add = true;
                    if (imgList.size() >= maxLimit || !addMode) {
                        entity = imgList.get(position);
                        add = false;
                    } else {
                        if (position > 0) {
                            entity = imgList.get(position - 1);
                            add = false;
                        } else
                            entity = null;
                    }
                    if (!add) {
                        if (uriType == UriType.Url)
                            ivImg.loadFromUrl(entity.getUri());
                        else if (uriType == UriType.Disk)
                            ivImg.loadLocalBitmap(entity.getUri());
                        if (imageClickListener != null) {
                            ivImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    imageClickListener.onClick(ivImg, position);
                                }
                            });
                        }
                        if (deleteAble) {
                            ivImg.setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if (deleteListener != null && !deleteListener.beforeDelete(position, entity)) {
                                        return true;
                                    }
                                    adapter.remove(position);
                                    imgList.remove(entity);
                                    notifyDataSetChanged();
                                    setHeight();
                                    if (deleteListener != null)
                                        deleteListener.afterDelete(position, entity);
                                    return true;
                                }
                            });
                        }
                    } else if (position == 0) {
                        if (ivBg != null) ivBg.setVisibility(View.GONE);
                        ivImg.setImageResource(R.drawable.bg_add);
                        if (addClickListener != null)
                            ivImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addClickListener.onClick();
                                }
                            });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        setAdapter(adapter);
    }

    private void setBg(final CImageView ivBg, final CImageView ivImg) {
        ivBg.setVisibility(VISIBLE);
        ivImg.setVisibility(GONE);
        ivImg.setLoadImageCallback(new CImageView.LoadImageCallback() {
            @Override
            public void onBefore() {
                ivBg.setVisibility(GONE);
                ivImg.setVisibility(VISIBLE);
            }

            @Override
            public void onSuccess(View v, Bitmap bm) {

            }

            @Override
            public void onFail() {

            }
        });
    }


    public interface ImageClickListener {
        void onClick(CImageView ivImg, int position);
    }

    private ImageClickListener imageClickListener;

    public void setImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public interface AddClickListener {
        void onClick();
    }

    private AddClickListener addClickListener;

    public void setAddClickListener(AddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }

    private DeleteListener deleteListener;

    public interface DeleteListener {
        boolean beforeDelete(int position, PhotoEntity entity);

        void afterDelete(int position, PhotoEntity entity);
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }


    public boolean isContainsKey(String key) {
        if (imgList == null || imgList.size() == 0) {
            return false;
        }
        for (PhotoEntity entity : imgList) {
            if (entity.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
}
