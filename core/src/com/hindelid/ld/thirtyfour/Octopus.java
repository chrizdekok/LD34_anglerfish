package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 13 Dec 2015.
 */
public class Octopus {

    Vector2 mPos = new Vector2();
    Rectangle mBoundingBox = new Rectangle(0, 0, 1f, 0.7f);

    public Octopus(float aX, float aY) {
        setPos(aX, aY);
    }

    public void setPos(float aX, float aY) {
        mPos.set(aX, aY);
        mBoundingBox.setPosition(mPos.x, mPos.y);
    }

    public void render(ShapeRenderer aShapeRenderer) {
        aShapeRenderer.curve(mPos.x, mPos.y,mPos.x+0.5f, mPos.y+1f, mPos.x+0.5f, mPos.y+1f,mPos.x+1f, mPos.y, 30 );
        aShapeRenderer.line(mPos.x, mPos.y, mPos.x + 1f, mPos.y);
        aShapeRenderer.triangle( // Left eye
                mPos.x + 0.3f, mPos.y + 0.4f,
                mPos.x + 0.35f, mPos.y + 0.45f,
                mPos.x + 0.45f, mPos.y + 0.32f);
        aShapeRenderer.triangle( // Right eye
                mPos.x + 0.7f, mPos.y + 0.4f,
                mPos.x + 0.65f, mPos.y + 0.45f,
                mPos.x + 0.55f, mPos.y + 0.32f);
        aShapeRenderer.line(mPos.x + 0.1f, mPos.y, mPos.x - 0.1f, mPos.y - 0.6f);
        aShapeRenderer.line(mPos.x+0.4f, mPos.y, mPos.x+0.35f, mPos.y - 0.6f);
        aShapeRenderer.line(mPos.x+0.6f, mPos.y, mPos.x+0.65f, mPos.y - 0.6f);
        aShapeRenderer.line(mPos.x+0.9f, mPos.y, mPos.x + 1.1f, mPos.y - 0.6f);
    }
}
