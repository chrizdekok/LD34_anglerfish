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

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 12 Dec 2015.
 */
public class Fish {

    private final float mRadus;
    final static float deg = 30f;
    Vector2 mPos;
    Vector2 mTailOffset;
    Vector2 mLampOffset;
    Rectangle mBoundingBox;

    public Fish(float aX, float aY) {
        mRadus = Constants.sRandom.nextFloat() * 0.4f + 0.4f;
        mPos = new Vector2();
        mTailOffset = new Vector2(MathUtils.cosDeg(deg) * mRadus, MathUtils.sinDeg(deg) * mRadus);
        mLampOffset = new Vector2(MathUtils.sinDeg(deg) * mRadus, MathUtils.cosDeg(deg) * mRadus);
        mBoundingBox = new Rectangle(0, 0, mRadus*2, mRadus*2);
        setPos(aX, aY);
    }

    public void setPos(float aX, float aY) {
        mPos.set(aX, aY);
        mBoundingBox.setPosition(mPos.x - mRadus, mPos.y - mRadus);
    }

    public void render(ShapeRenderer aShapeRenderer) {
        aShapeRenderer.circle(mPos.x, mPos.y, mRadus, 30); // Body
        aShapeRenderer.circle(mPos.x - 0.25f, mPos.y + 0.15f, 0.06f, 30); // Eye
        aShapeRenderer.line(mPos.x + mTailOffset.x, mPos.y + mTailOffset.y, mPos.x + 1f, mPos.y - 0.4f); // \
        aShapeRenderer.line(mPos.x + mTailOffset.x, mPos.y - mTailOffset.y, mPos.x+1f, mPos.y+0.4f); // /
        aShapeRenderer.line(mPos.x+1f, mPos.y-0.4f, mPos.x+1f, mPos.y+0.4f); // |
        aShapeRenderer.line(mPos.x - mLampOffset.x, mPos.y + mLampOffset.y, mPos.x - mLampOffset.x * 2f, mPos.y + mLampOffset.y * 2f);
        aShapeRenderer.line(mPos.x - mLampOffset.x * 3.3f, mPos.y + mLampOffset.y, mPos.x - mLampOffset.x * 2f, mPos.y + mLampOffset.y * 2f);
        aShapeRenderer.circle(mPos.x - mLampOffset.x * 3.3f, mPos.y + mLampOffset.y - 0.08f, 0.08f, 30);

        aShapeRenderer.curve(
                mPos.x-mTailOffset.x, mPos.y-mTailOffset.y,
                mPos.x-mTailOffset.x, mPos.y-mTailOffset.y*1.6f,
                mPos.x, mPos.y-mTailOffset.y*1.5f,
                mPos.x-mTailOffset.x/2f, mPos.y-mTailOffset.y/2f,
                30);
//        aShapeRenderer.rect(mBoundingBox.x, mBoundingBox.y, mBoundingBox.width, mBoundingBox.height);
    }


}
