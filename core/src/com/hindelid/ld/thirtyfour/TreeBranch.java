package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by chris on 12 Dec 2015.
 */
public class TreeBranch {

    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private Vector2 mLeefCord1;
    private Vector2 mLeefCord2;
    private Vector2 mLeefCord3;

    TreeBranch mLeftBranch = null;
    TreeBranch mRightBranch = null;

    public TreeBranch(float aX1, float aY1, float aX2, float aY2) {
        x1 = aX1;
        y1 = aY1;
        x2 = aX2;
        y2 = aY2;
        mLeefCord1 = new Vector2(x2 - x1, y2 - y1);
        mLeefCord2 = new Vector2(x2 - x1 + 0.1f, y2 - y1 + 0.1f);
        mLeefCord3 = new Vector2(x2 - x1 + 0.1f, y2 - y1 - 0.1f);
    }

    public void split() {
        if (null == mLeftBranch && null == mRightBranch) {
            double leftAngle = Constants.sRandom.nextDouble() * Math.PI / 2d;
            double rightAngle = Constants.sRandom.nextDouble() * Math.PI / 2d;
            mLeftBranch = new TreeBranch(x2, y2, x2 - (float)Math.sin(leftAngle), y2 + (float)Math.cos(leftAngle));
            mRightBranch = new TreeBranch(x2, y2, x2 + (float)Math.sin(rightAngle), y2 + (float)Math.cos(rightAngle));
        } else {
            mLeftBranch.split();
            mRightBranch.split();
        }
    }

    public void render(ShapeRenderer aShapeRenderer) {
        aShapeRenderer.line(x1, y1, x2, y2);

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
