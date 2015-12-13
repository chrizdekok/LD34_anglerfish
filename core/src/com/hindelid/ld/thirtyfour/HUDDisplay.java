package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by chris on 13 Dec 2015.
 */
public class HUDDisplay {

    private OrthographicCamera mHUDCamera;
    private Viewport mHUDViewport;
    private ShapeRenderer mShapeRenderer;
    private BitmapFont mFont;
    private Batch mBatch;

    private float[] leftArrow = {3,2, 3,3, 2,3, 2,4, 1,2.5f, 2,1, 2,2};
    private float[] rightArrow = {7,2, 7,3, 8,3, 8,4, 9,2.5f, 8,1, 8,2};

    private int mHealth;
    private int mCounter = 300;

    public HUDDisplay(ShapeRenderer aShapeRenderer) {
        mShapeRenderer = new ShapeRenderer();
        mBatch = new SpriteBatch();
        mFont = new BitmapFont();

        mFont.setColor(Color.RED);
        mFont.getData().setScale(0.1f);

        mHUDCamera = new OrthographicCamera();
        mHUDCamera.position.set(10/2, 10/2, 0);
        mHUDCamera.update();
        mHUDViewport = new ExtendViewport(10, 10, mHUDCamera);
        reset();
    }

    public void reset() {
        mHealth = 1;
        mCounter = 300;
    }

    public void resize(int aWidth, int aHeight) {
        mHUDViewport.update(aWidth, aHeight);
    }

    public void incHP() {
        mHealth++;
        mHealth = MathUtils.clamp(mHealth, 0, 5);
    }

    /**
     * @return true if dead.
     */
    public boolean decHP() {
        mHealth--;
        return mHealth <= 0;
    }

    public void renderStartScreen() {
        mHUDCamera.update();
        mShapeRenderer.setProjectionMatrix(mHUDCamera.combined);

        mShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        mShapeRenderer.setColor(Color.WHITE);
        mShapeRenderer.polygon(leftArrow);
        mShapeRenderer.polygon(rightArrow);
        mShapeRenderer.end();

    }

    public void render() {
        mHUDCamera.update();
        mShapeRenderer.setProjectionMatrix(mHUDCamera.combined);

        mShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        mShapeRenderer.setColor(Color.RED);
        mShapeRenderer.rect(0f, 0.5f, 5f, 0.5f);
        mShapeRenderer.setColor(Color.GREEN);
        mShapeRenderer.rect(0f, 0.5f, mHealth, 0.5f);
        mShapeRenderer.end();

    }

    public boolean renderGameOver(int aPoints) {
        mHUDCamera.update();
        mBatch.setProjectionMatrix(mHUDCamera.combined);

        mBatch.begin();
        mFont.getData().setScale(0.1f);
        mFont.draw(mBatch, "game over", 1.5f, 6f);
        mFont.getData().setScale(0.03f);
        mFont.draw(mBatch, "points:" + aPoints, 1.5f, 5f);
        mBatch.end();

        return mCounter-- <= 0;
    }

    public void dispose() {
        mBatch.dispose();
        mShapeRenderer.dispose();
    }

}
