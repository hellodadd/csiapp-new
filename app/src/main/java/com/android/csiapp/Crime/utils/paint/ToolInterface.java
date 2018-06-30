package com.android.csiapp.Crime.utils.paint;

/**
 * Created by user on 2016/12/16.
 */
import android.graphics.Canvas;

//所有画笔都应当实现这个接口
public interface ToolInterface {
    public void draw(Canvas canvas);

    public void touchDown(float x, float y);

    public void touchMove(float x, float y);

    public void touchUp(float x, float y);

    public boolean hasDraw();
}
