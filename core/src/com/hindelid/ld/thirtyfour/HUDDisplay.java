package com.hindelid.ld.thirtyfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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

    private float[] leftArrow = {120,80, 120,120, 80,120, 80,160, 40,100f, 80,40, 80,80};
    private float[] rightArrow = {280,80, 280,120, 320,120, 320,160, 360,100f, 320,40, 320,80};

    private int mHealth;
    private int mCounter;
    private int mHighScore = 0;

    public HUDDisplay() {
        mShapeRenderer = new ShapeRenderer();
        mBatch = new SpriteBatch();
        mFont = new BitmapFont();

        mFont.setColor(Color.RED);

        mHUDCamera = new OrthographicCamera();
        mHUDCamera.position.set(400/2, 400/2, 0);
        mHUDCamera.update();
        mHUDViewport = new ExtendViewport(400, 400, mHUDCamera);
        reset();
    }

    public void reset() {
        mHealth = 3;
        mCounter = 200;
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

    public void render(int aPoints) {
        mHUDCamera.update();
        mBatch.setProjectionMatrix(mHUDCamera.combined);

        mBatch.begin();
        mFont.getData().setScale(1f);
        mFont.draw(mBatch, "high score:" + mHighScore, 330f, 40f);
        mFont.draw(mBatch, "score:" + aPoints, 330f, 20f);
        mBatch.end();

        mShapeRenderer.setProjectionMatrix(mHUDCamera.combined);
        if (mHealth>0) {
            mShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            mShapeRenderer.setColor(Color.RED);
            mShapeRenderer.rect(0f, 20f, 200f, 20f);
            mShapeRenderer.setColor(Color.GREEN);
            mShapeRenderer.rect(0f, 20f, mHealth * 40f, 20f);
            mShapeRenderer.end();
        }
    }

    public boolean renderGameOver(int aPoints) {
        if (aPoints > mHighScore) {
            mHighScore = aPoints;
        }
        mHUDCamera.update();
        mBatch.setProjectionMatrix(mHUDCamera.combined);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            mCounter = 49;
        }
        if (mCounter < 50) {
            mCounter--;
        }
        mShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        mShapeRenderer.setColor(Color.BLACK);
        mShapeRenderer.rect(-2f, 0f, 130f, 40f);
        mShapeRenderer.end();

        mBatch.begin();
        if (mCounter >= 0) {
            mFont.getData().setScale(4f + 10f / (mCounter + 1f));
        } else {
            mFont.getData().setScale(4f);
        }
        mFont.draw(mBatch, "game over", 60f, 240f);
        mFont.getData().setScale(2f);
        mFont.draw(mBatch, "points:" + aPoints, 120f, 160f);
        mFont.getData().setScale(1f);
        mFont.setColor(Color.OLIVE);
        mFont.draw(mBatch, "made by chrizdekok", 0f, 40f);
        mFont.draw(mBatch, "www.hindelid.com", 0f, 20f);
        mFont.setColor(Color.RED);
        mBatch.end();

        return mCounter <= 0;
    }

    public void dispose() {
        mBatch.dispose();
        mShapeRenderer.dispose();
    }

}
