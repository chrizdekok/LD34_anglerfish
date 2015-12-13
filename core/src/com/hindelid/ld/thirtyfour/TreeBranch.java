/*******************************************************************************
 * Copyright 2014 Christoffer Hindelid. http://www.hindelid.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private TreeBranch mMiddleBranch = null;

    private boolean mActive;
    private boolean mTheOneActive;

    private boolean mFirstRenderer = true;


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
            double middleAngle = (Constants.sRandom.nextDouble() - 0.5d) * Math.PI / 4d;
            boolean leftActive = false;
            boolean rightActive = false;
            boolean growLeft = true;
            boolean growRight = true;
            mOrder++;
            if (mTheOneActive) {
                mOrder = 1;
                leftActive = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);

                if (!leftActive) {
                    rightActive = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
                }
            } else {
                growLeft = Constants.sRandom.nextInt(mOrder) < 2;
                growRight = Constants.sRandom.nextInt(mOrder) < 2;
            }
            if(mTheOneActive && !leftActive && !rightActive) {
                mMiddleBranch = new TreeBranch(mEnd,
                        new Vector2(
                                mEnd.x - (float) Math.sin(middleAngle) * Constants.SHRINKAGE_FACTOR / mOrder,
                                mEnd.y + (float) Math.cos(middleAngle) * Constants.SHRINKAGE_FACTOR / mOrder),
                        mTheOneActive, mOrder);
            }
            if (growLeft) {
                mLeftBranch = new TreeBranch(mEnd,
                        new Vector2(
                                mEnd.x - (float) Math.sin(leftAngle) * Constants.SHRINKAGE_FACTOR / mOrder,
                                mEnd.y + (float) Math.cos(leftAngle) * Constants.SHRINKAGE_FACTOR / mOrder),
                        mActive && leftActive, mOrder);
            }
            if (growRight) {
                mRightBranch = new TreeBranch(mEnd,
                        new Vector2(
                                mEnd.x + (float) Math.sin(rightAngle) * Constants.SHRINKAGE_FACTOR / mOrder,
                                mEnd.y + (float) Math.cos(rightAngle) * Constants.SHRINKAGE_FACTOR / mOrder),
                        mActive && rightActive, mOrder);
            }
            mTheOneActive = false;
        } else {
            if (null != mLeftBranch) {
                mLeftBranch.split();
            }
            if (null != mRightBranch) {
                mRightBranch.split();
            }
            if (null != mMiddleBranch) {
                mMiddleBranch.split();
            }
        }
    }

    public void render(ShapeRenderer aShapeRenderer) {
        if (mTheOneActive) {
            sGlobal.x = mStart.x;// + (mEnd.x - mStart.x) * (sGlobal.y - mStart.y) / (mEnd.y - mStart.y);
            if (sGlobal.y > mEnd.y) {
                sNext = true;
                if(mFirstRenderer) { // To keep up if the fps is lower than number of BranchTress grow per second.
                    split();
                }
            }
            mFirstRenderer = false;
        }
        if (mActive) {

//            aShapeRenderer.setColor(Color.RED);
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
            if (mEnd.y + 10f < sGlobal.y) {
                Main.mNextRoot = this;
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
        if (null != mMiddleBranch) {
            mMiddleBranch.render(aShapeRenderer);
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
        if (null != mMiddleBranch) {
            mMiddleBranch.renderLeefs(aShapeRenderer);
        }
    }

    public boolean checkCollision(Rectangle aBoundingBox) {
        return aBoundingBox.contains(mEnd) ||
                (null!=mLeftBranch && mLeftBranch.checkCollision(aBoundingBox)) ||
                (null!=mRightBranch && mRightBranch.checkCollision(aBoundingBox)) ||
                (null!=mMiddleBranch && mMiddleBranch.checkCollision(aBoundingBox));
    }

}
