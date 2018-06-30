package com.android.csiapp.Crime.utils.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PaintView extends View implements UndoCommand {

    boolean canvasIsCreated = false;

    private Canvas mCanvas = null;

    private ToolInterface mCurrentPainter = null;

    /* Bitmap 的配置 */
    private Bitmap mBitmap = null;

    private Bitmap mOrgBitMap = null;

    private int mBitmapWidth = 0;

    private int mBitmapHeight = 0;

    private int mBackGroundColor = PaintConstants.DEFAULT.BACKGROUND_COLOR;

    /* paint 的配置 */
    private Paint mBitmapPaint = null;

    private paintPadUndoStack mUndoStack = null;

    private int mPenColor = PaintConstants.DEFAULT.PEN_COLOR;;

    private int mPenSize = PaintConstants.PEN_SIZE.SIZE_1;

    private int mEraserSize = PaintConstants.ERASER_SIZE.SIZE_1;

    int mPaintType = PaintConstants.PEN_TYPE.PLAIN_PEN;

    private PaintViewCallBack mCallBack = null;

    private int mCurrentShapeType = 0;

    private ShapesInterface mCurrentShape = null;

    /**
     * 画笔样式为实心
     */
    private Paint.Style mStyle = Paint.Style.STROKE;

    /* 其他 的配置 */
    private boolean isTouchUp = false;
    /**
     * 是否已经畫圖
     */
    private boolean isTouched = false;

    private int mStackedSize = PaintConstants.UNDO_STACK_SIZE;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mCanvas = new Canvas();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mUndoStack = new paintPadUndoStack(this, mStackedSize);
        // 笔迹为铅笔
        mPaintType = PaintConstants.PEN_TYPE.PLAIN_PEN;
        // 曲线
        mCurrentShapeType = PaintConstants.SHAP.CURV;
        creatNewPen();
    }

    /**
     * 回调主函数的onHasDraw函数
     */
    public void setCallBack(PaintViewCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        isTouchUp = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCanvas.setBitmap(mBitmap);
                creatNewPen();
                mCurrentPainter.touchDown(x, y);
                mUndoStack.clearRedo();
                mCallBack.onTouchDown();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                isTouched = true;
                mCurrentPainter.touchMove(x, y);
                if (mPaintType == PaintConstants.PEN_TYPE.ERASER) {
                    mCurrentPainter.draw(mCanvas);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentPainter.hasDraw()) {
                    mUndoStack.push(mCurrentPainter);
                    if (mCallBack != null) {
                        // 控制undo\redo的现实
                        mCallBack.onHasDraw();
                    }
                }
                mCurrentPainter.touchUp(x, y);
                // 只有在up的时候才在bitmap上画图，最终显示在view上
                mCurrentPainter.draw(mCanvas);
                invalidate();
                isTouchUp = true;
                break;
        }
        return true;
    }

    /**
     * 设置具体形状，需要注意的是构造函数中的Painter必须是新鲜出炉的
     */
    private void setShape() {
        if (mCurrentPainter instanceof Shapable) {
            switch (mCurrentShapeType) {
                case PaintConstants.SHAP.CURV:
                    mCurrentShape = new Curv((Shapable)mCurrentPainter);
                    break;
                case PaintConstants.SHAP.LINE:
                    mCurrentShape = new Line((Shapable)mCurrentPainter);
                    break;
                case PaintConstants.SHAP.SQUARE:
                    mCurrentShape = new Square((Shapable)mCurrentPainter);
                    break;
                case PaintConstants.SHAP.RECT:
                    mCurrentShape = new Rectangle((Shapable)mCurrentPainter);
                    break;
                case PaintConstants.SHAP.CIRCLE:
                    mCurrentShape = new Circle((Shapable)mCurrentPainter);
                    break;
                case PaintConstants.SHAP.OVAL:
                    mCurrentShape = new Oval((Shapable)mCurrentPainter);
                    break;
                case PaintConstants.SHAP.STAR:
                    mCurrentShape = new Star((Shapable)mCurrentPainter);
                    break;
                default:
                    break;
            }
            ((Shapable)mCurrentPainter).setShap(mCurrentShape);
        }
    }

    @Override
    public void onDraw(Canvas cv) {
        cv.drawColor(mBackGroundColor);
        // 在外部绘制的方法只有一种，就是先在bitmap上绘制，然后加载到cv
        cv.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        // TouchUp使用BitMap的canvas进行绘制，也就不用再View上绘制了
        if (!isTouchUp) {
            // 平时都只在view的cv上临时绘制
            // earaser不能再cv上绘制，需要直接绘制在bitmap上
            if (mPaintType != PaintConstants.PEN_TYPE.ERASER) {
                mCurrentPainter.draw(cv);
            }
        }
    }

    /**
     * 创建一个新的画笔
     */
    void creatNewPen() {
        ToolInterface tool = null;
        switch (mPaintType) {
            case PaintConstants.PEN_TYPE.PLAIN_PEN:
                tool = new PlainPen(mPenSize, mPenColor, mStyle);
                break;
            case PaintConstants.PEN_TYPE.ERASER:
                tool = new Eraser(mEraserSize);
                break;
            case PaintConstants.PEN_TYPE.BLUR:
                tool = new BlurPen(mPenSize, mPenColor, mStyle);
                break;
            case PaintConstants.PEN_TYPE.EMBOSS:
                tool = new EmbossPen(mPenSize, mPenColor, mStyle);
                break;
            default:
                break;
        }
        mCurrentPainter = tool;
        setShape();
    }

    /**
     * 当此事件发生时，创建Bitmap并setCanvas
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!canvasIsCreated) {
            mBitmapWidth = w;
            mBitmapHeight = h;
            creatCanvasBitmap(w, h);
            canvasIsCreated = true;
            isTouched=false;
        }
    }

    /**
     * 创建画布上的背景图片，同时留有备份
     */
    public void setForeBitMap(Bitmap bitmap) {
        if (bitmap != null) {
            recycleMBitmap();
            recycleOrgBitmap();
        }
        mBitmap = BitMapUtils.duplicateBitmap(bitmap, getWidth(), getHeight());
        mOrgBitMap = BitMapUtils.duplicateBitmap(mBitmap);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        invalidate();
    }

    /**
     * 回收原始图片
     */
    private void recycleOrgBitmap() {
        if (mOrgBitMap != null && !mOrgBitMap.isRecycled()) {
            mOrgBitMap.recycle();
            mOrgBitMap = null;
        }
    }

    /**
     * 回收图片
     */
    private void recycleMBitmap() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    /**
     * 得到当前view的截图
     */
    public Bitmap getSnapShoot() {
        // 获得当前的view的图片
        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        Bitmap bitmap = getDrawingCache(true);
        Bitmap bmp = BitMapUtils.duplicateBitmap(bitmap);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        // 将缓存清理掉
        setDrawingCacheEnabled(false);
        return bmp;
    }

    /**
     * 清空屏幕
     *
     * @param clearBackground 是否要清除背景图片
     */
    public void clearAll(boolean clearBackground) {
        if (clearBackground) {// 清理原来的背景
            recycleMBitmap();
            recycleOrgBitmap();
            creatCanvasBitmap(mBitmapWidth, mBitmapHeight);
        } else {// 不清除原来的背景
            if (mOrgBitMap != null) {
                mBitmap = BitMapUtils.duplicateBitmap(mOrgBitMap);
                mCanvas.setBitmap(mBitmap);
            } else {
                creatCanvasBitmap(mBitmapWidth, mBitmapHeight);
            }
        }
        isTouched = false;
        mUndoStack.clearAll();
        invalidate();
    }

    /**
     * 创建bitMap同时获得其canvas
     */
    private void creatCanvasBitmap(int w, int h) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
    }

    /**
     * 改变当前画笔的类型
     */
    public void setCurrentPainterType(int type) {
        switch (type) {
            case PaintConstants.PEN_TYPE.BLUR:
            case PaintConstants.PEN_TYPE.PLAIN_PEN:
            case PaintConstants.PEN_TYPE.EMBOSS:
            case PaintConstants.PEN_TYPE.ERASER:
                mPaintType = type;
                break;
            default:
                mPaintType = PaintConstants.PEN_TYPE.PLAIN_PEN;
                break;
        }
    }

    /**
     * 改变当前的Shap
     */
    public void setCurrentShapType(int type) {
        switch (type) {
            case PaintConstants.SHAP.CURV:
            case PaintConstants.SHAP.LINE:
            case PaintConstants.SHAP.RECT:
            case PaintConstants.SHAP.CIRCLE:
            case PaintConstants.SHAP.OVAL:
            case PaintConstants.SHAP.SQUARE:
            case PaintConstants.SHAP.STAR:
                mCurrentShapeType = type;
                break;
            default:
                mCurrentShapeType = PaintConstants.SHAP.CURV;
                break;
        }
    }

    /**
     * 得到当前画笔的类型
     */
    public int getCurrentPainter() {
        return mPaintType;
    }

    /**
     * 改变当前画笔的大小
     */
    public void setPenSize(int size) {
        mPenSize = size;
    }

    /**
     * 改变当前Eraser的大小
     */
    public void setEraserSize(int size) {
        mEraserSize = size;
    }

    /**
     * 得到当前画笔的大小
     */
    public int getPenSize() {
        return mPenSize;
    }

    /**
     * 重置状态
     */
    public void resetState() {
        setCurrentPainterType(PaintConstants.PEN_TYPE.PLAIN_PEN);
        setPenColor(PaintConstants.DEFAULT.PEN_COLOR);
        setBackGroundColor(PaintConstants.DEFAULT.BACKGROUND_COLOR);
        mUndoStack.clearAll();
    }

    /**
     * 更改背景颜色
     */
    public void setBackGroundColor(int color) {
        mBackGroundColor = color;
        invalidate();
    }

    /**
     * 得到背景颜色
     */
    public int getBackGroundColor() {
        return mBackGroundColor;
    }

    /**
     * 改变画笔的颜色，在创建新笔的时候就能使用了
     */
    public void setPenColor(int color) {
        mPenColor = color;
    }

    /**
     * 得到penColor
     */
    public int getPenColor() {
        return mPenColor;
    }

    /**
     * 创建临时背景，没有备份
     */
    protected void setTempForeBitmap(Bitmap tempForeBitmap) {
        if (null != tempForeBitmap) {
            recycleMBitmap();
            mBitmap = BitMapUtils.duplicateBitmap(tempForeBitmap);
            if (null != mBitmap && null != mCanvas) {
                mCanvas.setBitmap(mBitmap);
                invalidate();
            }
        }
    }

    public void setPenStyle(Style style) {
        mStyle = style;
    }

    public byte[] getBitmapArry() {
        return BitMapUtils.bitampToByteArray(mBitmap);
    }

    @Override
    public void undo() {
        Log.d("Anita","undo");
        if (null != mUndoStack) {
            mUndoStack.undo();
        }
    }

    @Override
    public void redo() {
        Log.d("Anita","redo");
        if (null != mUndoStack) {
            mUndoStack.redo();
        }
    }

    @Override
    public boolean canUndo() {
        return mUndoStack.canUndo();
    }

    @Override
    public boolean canRedo() {
        return mUndoStack.canRedo();
    }

    public void save(String path) throws IOException {
        save(path,0);
    }

    public void save(String path, int blank) throws IOException {

        Bitmap bitmap=mBitmap;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
    }

    /**
     * 是否有畫圖
     *
     * @return
     */
    public boolean getTouched() {
        return isTouched;
    }

    @Override
    public String toString() {
        return "mPaint" + mCurrentPainter + mUndoStack;
    }

    /*
     * ===================================内部类开始=================================
     * 内部类，负责undo、redo
     */
    public class paintPadUndoStack {
        private int m_stackSize = 0;

        private PaintView mPaintView = null;

        private ArrayList<ToolInterface> mUndoStack = new ArrayList<ToolInterface>();

        private ArrayList<ToolInterface> mRedoStack = new ArrayList<ToolInterface>();

        private ArrayList<ToolInterface> mOldActionStack = new ArrayList<ToolInterface>();

        public paintPadUndoStack(PaintView paintView, int stackSize) {
            mPaintView = paintView;
            m_stackSize = stackSize;
        }

        /**
         * 将painter存入栈中
         */
        public void push(ToolInterface penTool) {
            if (null != penTool) {
                // 如果undo已经存满
                if (mUndoStack.size() == m_stackSize && m_stackSize > 0) {
                    // 得到最远的画笔
                    ToolInterface removedTool = mUndoStack.get(0);
                    // 所有的笔迹增加
                    mOldActionStack.add(removedTool);
                    mUndoStack.remove(0);
                }

                mUndoStack.add(penTool);
            }
        }

        /**
         * 清空所有
         */
        public void clearAll() {
            isTouched = false;
            mRedoStack.clear();
            mUndoStack.clear();
            mOldActionStack.clear();
        }

        /**
         * undo
         */
        public void undo() {
            if (canUndo() && null != mPaintView) {
                ToolInterface removedTool = mUndoStack.get(mUndoStack.size() - 1);
                mRedoStack.add(removedTool);
                mUndoStack.remove(mUndoStack.size() - 1);

                if (null != mOrgBitMap) {
                    // Set the temporary fore bitmap to canvas.
                    // 当载入文件时保存了一份,现在要重新绘制出来
                    mPaintView.setTempForeBitmap(mPaintView.mOrgBitMap);
                } else {
                    // 如果背景不存在，则重新创建一份背景
                    mPaintView.creatCanvasBitmap(mPaintView.mBitmapWidth, mPaintView.mBitmapHeight);
                }

                Canvas canvas = mPaintView.mCanvas;

                // First draw the removed tools from undo stack.
                for (ToolInterface paintTool : mOldActionStack) {
                    paintTool.draw(canvas);
                }

                for (ToolInterface paintTool : mUndoStack) {
                    paintTool.draw(canvas);
                }

                mPaintView.invalidate();
            }
        }

        /**
         * redo
         */
        public void redo() {
            if (canRedo() && null != mPaintView) {
                ToolInterface removedTool = mRedoStack.get(mRedoStack.size() - 1);
                mUndoStack.add(removedTool);
                mRedoStack.remove(mRedoStack.size() - 1);

                if (null != mOrgBitMap) {
                    // Set the temporary fore bitmap to canvas.
                    mPaintView.setTempForeBitmap(mPaintView.mOrgBitMap);
                } else {
                    // Create a new bitmap and set to canvas.
                    mPaintView.creatCanvasBitmap(mPaintView.mBitmapWidth, mPaintView.mBitmapHeight);
                }

                Canvas canvas = mPaintView.mCanvas;
                // 所有以前的笔迹都存放在removedStack中
                // First draw the removed tools from undo stack.
                for (ToolInterface sketchPadTool : mOldActionStack) {
                    sketchPadTool.draw(canvas);
                }
                // 不管怎样都是从撤销里面绘制，重做只是暂时的存储
                for (ToolInterface sketchPadTool : mUndoStack) {
                    sketchPadTool.draw(canvas);
                }

                mPaintView.invalidate();
            }
        }

        public boolean canUndo() {
            return (mUndoStack.size() > 0);
        }

        public boolean canRedo() {
            return (mRedoStack.size() > 0);
        }

        public void clearRedo() {
            mRedoStack.clear();
        }

        @Override
        public String toString() {
            return "canUndo" + canUndo();
        }
    }
    /* ==================================内部类结束 ================================= */

}
