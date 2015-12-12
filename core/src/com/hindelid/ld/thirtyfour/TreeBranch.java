package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 12 Dec 2015.
 */
public class TreeBranch {

    public static Vector2 sGlobal = new Vector2();
    public static boolean sNext = false;

    private Vector2 mStart;
    private Vector2 mEnd;

    private int mOrder;
    private boolean mSplitted = false;

    private Vector2 mLeefCord1;
    private Vector2 mLeefCord2;
    private Vector2 mLeefCord3;

    private TreeBranch mLeftBranch = null;
    private TreeBranch mRightBranch = null;

    private boolean mActive;
    private boolean mTheOneActive;


    public TreeBranch(Vector2 aStart, Vector2 aEnd, boolean aActive, int aOrder) {
        mStart = aStart;
        mEnd = aEnd;
        mLeefCord1 = new Vector2(aStart.x + (aEnd.x - aStart.x)/2f, aStart.y + (aEnd.y - aStart.y) / 2f);
        mLeefCord2 = new Vector2(aStart.x + (aEnd.x - aStart.x)*1.1f/2f, aStart.y + (aEnd.y - aStart.y)*1.1f / 2f);
        mLeefCord3 = new Vector2(aStart.x + (aEnd.x - aStart.x)*1.1f/2f, aStart.y + (aEnd.y - aStart.y)*0.9f / 2f);
        mActive = aActive;
        mTheOneActive = aActive;
        mOrder = aOrder;
    }

    public void split() {
        if (!mSplitted) {
            mSplitted = true;
            double leftAngle = (Constants.sRandom.nextDouble() + 0.5d) * Math.PI / 4d;
            double rightAngle = (Constants.sRandom.nextDouble() + 0.5d) * Math.PI / 4d;
            boolean leftActive = false;
            boolean growLeft = true;
            boolean growRight = true;
            mOrder++;
            if (mTheOneActive) {
                leftActive = Constants.sRandom.nextBoolean();
            } else {
                growLeft = Constants.sRandom.nextInt(mOrder) < 2;
                growRight = Constants.sRandom.nextInt(mOrder) < 2;
            }
            if (growLeft) {
                mLeftBranch = new TreeBranch(mEnd,
                        new Vector2(
                                mEnd.x - (float) Math.sin(leftAngle) * Constants.SHRINKAGE_FACTOR,
                                mEnd.y + (float) Math.cos(leftAngle) * Constants.SHRINKAGE_FACTOR),
                        mActive && leftActive, mOrder);
            }
            if (growRight) {
                mRightBranch = new TreeBranch(mEnd,
                        new Vector2(
                                mEnd.x + (float) Math.sin(rightAngle) * Constants.SHRINKAGE_FACTOR,
                                mEnd.y + (float) Math.cos(rightAngle) * Constants.SHRINKAGE_FACTOR),
                        mActive && !leftActive, mOrder);
            }
            mTheOneActive = false;
        } else {
            if (null != mLeftBranch) {
                mLeftBranch.split();
            }
            if (null != mRightBranch) {
                mRightBranch.split();
            }
        }
    }

    public void render(ShapeRenderer aShapeRenderer) {
        if (mTheOneActive) {
            sGlobal.x = mStart.x + (mEnd.x - mStart.x) * (sGlobal.y - mStart.y) / (mEnd.y - mStart.y);
            if (sGlobal.y > mEnd.y) {
                sNext = true;
            }
        }
        if (mActive) {

            aShapeRenderer.setColor(Color.RED);
            if (mTheOneActive && sGlobal.y < mEnd.y) {
                aShapeRenderer.line(
                        mStart.x,
                        mStart.y,
                        sGlobal.x,
                        sGlobal.y
                        /*mStart.x + (sGlobal.x - mStart.x) / (mEnd.x - mStart.x),
                        mStart.y + (sGlobal.y - mStart.y) / (mEnd.y - mStart.y)*/);
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
