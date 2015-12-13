package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Font;

/**
 * Created by chris on 13 Dec 2015.
 */
public class HUDDisplay {

    private OrthographicCamera mHUDCamera;
    private Viewport mHUDViewport;
    private ShapeRenderer mShapeRenderer;
    private BitmapFont mFont;
    private Batch mBatch;

    private float[] leftArrow = {30,20, 30,30, 20,30, 20,40, 10,25f, 20,10, 20,20};
    private float[] rightArrow = {70,20, 70,30, 80,30, 80,40, 90,25f, 80,10, 80,20};

    private int mHealth;
    private int mCounter = 100;

    public HUDDisplay(ShapeRenderer aShapeRenderer) {
        mShapeRenderer = new ShapeRenderer();
        mBatch = new SpriteBatch();
        mFont = new BitmapFont();

        mFont.setColor(Color.RED);
        //mFont.getData().setScale(0.1f);

        mHUDCamera = new OrthographicCamera();
        mHUDCamera.position.set(100/2, 100/2, 0);
        mHUDCamera.update();
        mHUDViewport = new ExtendViewport(100, 100, mHUDCamera);
        reset();
    }

    public void reset() {
        mHealth = 1;
        mCounter = 100;
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
        mBatch.setProjectionMatrix(mHUDCamera.combined);

        mBatch.begin();
        //mFont.getData().setScale(0.5f);
        mFont.draw(mBatch, "high score:" + 123, 75f, 15f);
        mFont.draw(mBatch, "score:" + 44, 75f, 5f);
        mBatch.end();

        mShapeRenderer.setProjectionMatrix(mHUDCamera.combined);

        mShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        mShapeRenderer.setColor(Color.RED);
        mShapeRenderer.rect(0f, 5f, 50f, 5f);
        mShapeRenderer.setColor(Color.GREEN);
        mShapeRenderer.rect(0f, 5f, mHealth * 10f, 5f);
        mShapeRenderer.end();

    }

    public boolean renderGameOver(int aPoints) {
        mHUDCamera.update();
        mBatch.setProjectionMatrix(mHUDCamera.combined);

        mBatch.begin();
        if (mCounter >= 0) {
            mFont.getData().setScale(1f + 5f / (mCounter + 1f));
        } else {
            mFont.getData().setScale(1f);
        }
        mFont.draw(mBatch, "game over", 15f, 60f);
        mFont.getData().setScale(0.5f);
        mFont.draw(mBatch, "points:" + aPoints, 30f, 40f);
        mBatch.end();

        return mCounter-- <= 0;
    }

    public void dispose() {
        mBatch.dispose();
        mShapeRenderer.dispose();
    }

}
