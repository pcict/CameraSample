package com.websarva.wings.android.camerasample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.mlkit.vision.face.Face;

import java.util.ArrayList;
import java.util.List;

public class FaceDetectionView extends View {
    private List<Face> faces = new ArrayList<>();
    private Paint paint;
    private int inputImageWidth, inputImageHeight;

    public FaceDetectionView(Context context) {
        super(context);
        init();
    }

    public FaceDetectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setFaces(List<Face> detectedFaces, int width, int height) {
        faces = detectedFaces;
        inputImageWidth = width;
        inputImageHeight = height;
        invalidate(); // 再描画を要求
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            int left = (bounds.left * getWidth() / inputImageWidth) + 200;
            int top = (bounds.top * getHeight() / inputImageHeight) - 400;
            int right = (bounds.right * getWidth() / inputImageWidth) + 200;
            int bottom = (bounds.bottom * getHeight() / inputImageHeight) - 400;
            canvas.drawRect(left, top, right, bottom, paint);
        }
//        for (Face face : faces) {
//            Rect bounds = face.getBoundingBox();
//            int left = bounds.left * getWidth() / inputImageWidth;
//            int top = bounds.top * getHeight() / inputImageHeight;
//            int right = bounds.right * getWidth() / inputImageWidth;
//            int bottom = bounds.bottom * getHeight() / inputImageHeight;
//            canvas.drawRect(left, top, right, bottom, paint);
//        }
    }

    private RectF adjustRectForCameraRotation(Rect rect, int rotation, int viewWidth, int viewHeight) {
        RectF adjustedRect = new RectF(rect);
        switch (rotation) {
            case 0:
                break;
            case 90:
                adjustedRect.set(adjustedRect.top, viewWidth - adjustedRect.right, adjustedRect.bottom, viewWidth - adjustedRect.left);
                break;
            case 180:
                adjustedRect.set(viewWidth - adjustedRect.right, viewHeight - adjustedRect.bottom, viewWidth - adjustedRect.left, viewHeight - adjustedRect.top);
                break;
            case 270:
                adjustedRect.set(viewHeight - adjustedRect.bottom, adjustedRect.left, viewHeight - adjustedRect.top, adjustedRect.right);
                break;
        }
        return adjustedRect;
    }

    public void logFacePositions() {
        for (Face face : faces) {
            Rect bounds = face.getBoundingBox();
            int left = bounds.left * getWidth() / inputImageWidth;
            int top = bounds.top * getHeight() / inputImageHeight;
            int right = bounds.right * getWidth() / inputImageWidth;
            int bottom = bounds.bottom * getHeight() / inputImageHeight;
            Log.d("CameraSample", "Face position: left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom);
        }
        Log.d("CameraSample", "Number of faces: " + faces.size());
    }

}
