package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 12 Dec 2015.
 */
public class Fish {

    final static float RADIE = 0.5f;
    final static float deg = 30f;
    Vector2 mPos;
    Vector2 mTailOffset = new Vector2(MathUtils.cosDeg(deg) * RADIE, MathUtils.sinDeg(deg) * RADIE);
    Vector2 mLampOffset = new Vector2(MathUtils.sinDeg(deg) * RADIE, MathUtils.cosDeg(deg) * RADIE);

    public Fish(float aX, float aY) {
        mPos = new Vector2(aX, aY);
    }

    public void render(ShapeRenderer aShapeRenderer) {
        aShapeRenderer.circle(mPos.x, mPos.y, RADIE, 30); // Body
        aShapeRenderer.circle(mPos.x - 0.25f, mPos.y + 0.15f, 0.06f, 30); // Eye
        aShapeRenderer.line(mPos.x + mTailOffset.x, mPos.y + mTailOffset.y, mPos.x + 1f, mPos.y - 0.4f); // \
        aShapeRenderer.line(mPos.x + mTailOffset.x, mPos.y - mTailOffset.y, mPos.x+1f, mPos.y+0.4f); // /
        aShapeRenderer.line(mPos.x+1f, mPos.y-0.4f, mPos.x+1f, mPos.y+0.4f); // |
        aShapeRenderer.line(mPos.x - mLampOffset.x, mPos.y + mLampOffset.y, mPos.x - mLampOffset.x * 2f, mPos.y + mLampOffset.y * 2f);
        aShapeRenderer.line(mPos.x - mLampOffset.x * 3.3f, mPos.y + mLampOffset.y, mPos.x - mLampOffset.x * 2f, mPos.y + mLampOffset.y * 2f);
        aShapeRenderer.circle(mPos.x - mLampOffset.x * 3.3f, mPos.y + mLampOffset.y - 0.08f, 0.08f, 30);
    }

}
