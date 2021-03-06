package com.android.csiapp.Crime.utils.paint;

import android.graphics.Color;
import android.os.Environment;

/**
 * Created by user on 2016/12/16.
 */
public class PaintConstants {
    private PaintConstants() {

    }

    public static final class ERASER_SIZE {
        public static final int SIZE_1 = 5;

        public static final int SIZE_2 = 10;

        public static final int SIZE_3 = 15;

        public static final int SIZE_4 = 20;

        public static final int SIZE_5 = 30;
    }

    public static final class PEN_SIZE {
        public static final int SIZE_1 = 5;

        public static final int SIZE_2 = 10;

        public static final int SIZE_3 = 15;

        public static final int SIZE_4 = 20;

        public static final int SIZE_5 = 30;
    }

    public static final class SHAP {
        /**
         * 曲线
         */
        public static final int CURV = 1;

        /**
         * 直线
         */
        public static final int LINE = 2;

        /**
         * 矩形
         */
        public static final int RECT = 3;

        /**
         * 园
         */
        public static final int CIRCLE = 4;

        /**
         * 椭圆
         */
        public static final int OVAL = 5;

        /**
         * 正方形
         */
        public static final int SQUARE = 6;

        /**
         * 五角星
         */
        public static final int STAR = 7;
    }

    public static final class PATH {
        public static final String SAVE_PATH = Environment.getExternalStorageDirectory().getPath()
                + "/paintPad";
    }

    public static final class PEN_TYPE {
        /**
         * 铅笔
         */
        public static final int PLAIN_PEN = 1;

        /**
         * 橡皮擦
         */
        public static final int ERASER = 2;

        /**
         * 模糊
         */
        public static final int BLUR = 3;

        /**
         * 浮雕
         */
        public static final int EMBOSS = 4;
    }

    public static final class DEFAULT {
        public static final int PEN_COLOR = Color.BLACK;

        public static final int BACKGROUND_COLOR = Color.WHITE;
    }

    public static final int UNDO_STACK_SIZE = 20;

    public static final int COLOR_VIEW_SIZE = 80;

    public static final int LOAD_ACTIVITY = 1;

    public static final int COLOR1 = Color.argb(255, 44, 152, 140);

    public static final int COLOR2 = Color.argb(255, 48, 115, 170);

    public static final int COLOR3 = Color.argb(255, 139, 26, 99);

    public static final int COLOR4 = Color.argb(255, 112, 101, 89);

    public static final int COLOR5 = Color.argb(255, 40, 36, 37);

    public static final int COLOR6 = Color.argb(255, 226, 226, 226);

    public static final int COLOR7 = Color.argb(255, 219, 88, 50);

    public static final int COLOR8 = Color.argb(255, 129, 184, 69);
}
