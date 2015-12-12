package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 12 Dec 2015.
 */
public class TreeBranch {

    public static Vector2 sGlobal = new Vector2();

    private Vector2 mStart;
    private Vector2 mEnd;
//    private float x1;
//    private float y1;
//    private float x2;
//    private float y2;

    private Vector2 mLeefCord1;
    private Vector2 mLeefCord2;
    private Vector2 mLeefCord3;

    private TreeBranch mLeftBranch = null;
    private TreeBranch mRightBranch = null;

    private boolean mActive;
    private boolean mTheOneActive;


    public TreeBranch(Vector2 aStart, Vector2 aEnd, boolean aActive) {
        mStart = aStart;
        mEnd = aEnd;
        mLeefCord1 = new Vector2(aStart.x + (aEnd.x - aStart.x)/2f, aStart.y + (aEnd.y - aStart.y) / 2f);
        mLeefCord2 = new Vector2(aStart.x + (aEnd.x - aStart.x)*1.1f/2f, aStart.y + (aEnd.y - aStart.y)*1.1f / 2f);
        mLeefCord3 = new Vector2(aStart.x + (aEnd.x - aStart.x)*1.1f/2f, aStart.y + (aEnd.y - aStart.y)*0.9f / 2f);
        mActive = aActive;
        mTheOneActive = aActive;
    }

    public void split() {
        if (null == mLeftBranch && null == mRightBranch) {
            double leftAngle = (Constants.sRandom.nextDouble() + 0.5d) * Math.PI / 4d;
            double rightAngle = (Constants.sRandom.nextDouble() + 0.5d) * Math.PI / 4d;
            boolean leftActive = false;
            if (mActive) {
                leftActive = Constants.sRandom.nextBoolean();
            }

            mLeftBranch = new TreeBranch(mEnd,
                    new Vector2(
                            mEnd.x - (float)Math.sin(leftAngle) * Constants.SHRINKAGE_FACTOR,
                            mEnd.y + (float)Math.cos(leftAngle) * Constants.SHRINKAGE_FACTOR),
                    mActive && leftActive);
            mRightBranch = new TreeBranch(mEnd,
                    new Vector2(
                            mEnd.x + (float)Math.sin(rightAngle) * Constants.SHRINKAGE_FACTOR,
                            mEnd.y + (float)Math.cos(rightAngle) * Constants.SHRINKAGE_FACTOR),
                    mActive && !leftActive);
            mTheOneActive = false;
        } else {
            mLeftBranch.split();
            mRightBranch.split();
        }
    }

    public void render(ShapeRenderer aShapeRenderer) {
        if (mTheOneActive) {
            sGlobal.x = mStart.x;
        }
        if (mActive) {

            aShapeRenderer.setColor(Color.RED);
            if (sGlobal.y < mEnd.y) {

            } else {
                aShapeRenderer.line(mStart, mEnd);
            }
            aShapeRenderer.setColor(Color.BROWN);
        } else {
            aShapeRenderer.line(mStart, mEnd);
        }


        if (null != mLeftBranch) {
            mLeftBranch.render(aShapeRenderer);
        }
        if (null != mRightBranch) {
            mRightBranch.render(aShapeRenderer);
        }

    }

    public void renderLeefs(ShapeRenderer aShapeRenderer) {
        aShapeRenderer.triangle(mLeefCord1.x, mLeefCord1.y, mLeefCord2.x, mLeefCord2.y, mLeefCord3.x, mLeefCord3.y);
        if (null != mLeftBranch) {
            mLeftBranch.renderLeefs(aShapeRenderer);
        }
        if (null != mRightBranch) {
            mRightBranch.renderLeefs(aShapeRenderer);
        }
    }

}
