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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends ApplicationAdapter {
    private Viewport mViewPort;
    private OrthographicCamera mCamera;
    private ShapeRenderer mShapeRenderer;

    private TreeBranch mRoot;
    public static TreeBranch mNextRoot = null;

    private Vector2 mCurrentViewCord;
    private List<Fish> mFishes = new ArrayList<Fish>();
    private List<Octopus> mOctopuses = new ArrayList<Octopus>();
    private HUDDisplay mHUDDisplay;

    private float mAvgX[] = new float[10];
    private int mAvgXpos = 0;

    private static Sound mStartSound;
    private static Sound mDeadSound;
    private static Sound mOctoSound;
    private static Sound mFishSound;

    private float mSpeed;
    private float mTotalPoints = 0f;

    private boolean mDead = false;

    @Override
    public void create() {
        mShapeRenderer = new ShapeRenderer();
        mHUDDisplay = new HUDDisplay();
        mCamera = new OrthographicCamera();
        mViewPort = new ExtendViewport(Constants.VIEW_SIZE_X, Constants.VIEW_SIZE_Y, mCamera);
        mCurrentViewCord = new Vector2(Constants.VIEW_SIZE_X / 2, Constants.VIEW_SIZE_Y / 2);
        moveAndUpdateCamera();
        mStartSound = Gdx.audio.newSound(Gdx.files.internal("start.wav"));
        mDeadSound = Gdx.audio.newSound(Gdx.files.internal("dead.wav"));
        mOctoSound = Gdx.audio.newSound(Gdx.files.internal("octo.wav"));
        mFishSound = Gdx.audio.newSound(Gdx.files.internal("fish.wav"));
        resetGame();
    }

    @Override
    public void dispose() {
        mHUDDisplay.dispose();
        mStartSound.dispose();
        mDeadSound.dispose();
        mOctoSound.dispose();
        mFishSound.dispose();
        super.dispose();
    }

    @Override
    public void resize(int aWidth, int aHeight) {
        mViewPort.update(aWidth, aHeight);
        mHUDDisplay.resize(aWidth, aHeight);
    }

    private void resetGame() {
        for(int i=0;i<10;i++) {
            mAvgX[i] = 0;
        }
        mRoot = new TreeBranch(
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0),
                new Vector2(Constants.VIEW_SIZE_X / 2f, 0.5f),
                true,
                1);
        mNextRoot = null;
        mCamera.zoom = 1.0f;
        TreeBranch.sGlobal.setZero();
        mSpeed = Constants.SPEED;
        mHUDDisplay.reset();
        mDead = false;
        mTotalPoints = 0;

        mStartSound.play();
    }

    @Override
    public void render() {

        long before = TimeUtils.nanoTime();
        tick();
        if (TreeBranch.sGlobal.y < 1000f) {
            Gdx.gl.glClearColor(0, 0, TreeBranch.sGlobal.y / 1000f, 1);
        } else if (TreeBranch.sGlobal.y < 2000f) {
            Gdx.gl.glClearColor((TreeBranch.sGlobal.y-1000f) / 1000f, 0, 1, 1);
        } else {
            Gdx.gl.glClearColor((3000f - TreeBranch.sGlobal.y) / 1000f, 0, (3000f - TreeBranch.sGlobal.y) / 1000f, 1);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mShapeRenderer.setProjectionMatrix(mCamera.combined);

        mShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        mShapeRenderer.setColor(Color.BROWN);
        mRoot.render(mShapeRenderer);

        mShapeRenderer.setColor(Color.GREEN);
        mRoot.renderLeefs(mShapeRenderer);

        mShapeRenderer.setColor(Color.WHITE);
        for (Fish f : mFishes) {
            f.render(mShapeRenderer);
        }
        for (Octopus o : mOctopuses) {
            o.render(mShapeRenderer);
        }

        mShapeRenderer.end();

        mHUDDisplay.render((int)mTotalPoints);

        long after = TimeUtils.nanoTime();


        //System.out.println("speed:" + mSpeed + " y:" + TreeBranch.sGlobal.y + " Time:" + (after - before) / 1000); //TODO remove

        if (TreeBranch.sGlobal.y < 15f) {
            mHUDDisplay.renderStartScreen();
        }
        if (mDead) {
            if (mHUDDisplay.renderGameOver((int)mTotalPoints)) {
                resetGame();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void tick() {

        checkForNewRoot();
        spawnNewStuff();
        moveAndUpdateCamera();
        if (TreeBranch.sNext) {
            TreeBranch.sNext = false;
            mRoot.split();
        }
        Iterator<Fish> fishIter = mFishes.iterator();
        while (fishIter.hasNext()) {
            if (mRoot.checkCollision(fishIter.next().mBoundingBox)) {
                mTotalPoints += 10;
                mFishSound.play();
                fishIter.remove();
                mHUDDisplay.incHP();
            }
        }
        Iterator<Octopus> octoIter = mOctopuses.iterator();
        while (octoIter.hasNext()) {
            if (mRoot.checkCollision(octoIter.next().mBoundingBox)) {
                octoIter.remove();
                mTotalPoints -= 10;

                if (mHUDDisplay.decHP()) {
                    mDead = true;
                    mDeadSound.play();
                } else {
                    mOctoSound.play();
                }
            }
        }

        removeObsticles();

        if (mDead) {
            mSpeed = 0;
        } else {
            mSpeed = Constants.SPEED + TreeBranch.sGlobal.y/2000f;
        }

    }

    /**
     * To avoid unnecessary CPU time when change the root.
     */
    private void checkForNewRoot() {
        if (mNextRoot != null && mNextRoot != mRoot) {
            mRoot = mNextRoot;
        }
    }

    private void spawnNewStuff() {
        if (TreeBranch.sGlobal.y < 10f) { //Don't spawn anything in the beginning.
            return;
        }
        float ypos = TreeBranch.sGlobal.y;
        if ((ypos * 150f / 800f + 30f) * Constants.sRandom.nextFloat() < 1f) {
            spawnFishAbove();
        }
        if (Constants.sRandom.nextFloat() * 100f - (50f * ypos / 1024f) < 1f) {
            spawnOctoAbove();
        }
    }

    private void removeObsticles() {
        Iterator<Fish> fishIter = mFishes.iterator();
        while (fishIter.hasNext()) {
            if (fishIter.next().mPos.y < TreeBranch.sGlobal.y - 10f) {
                fishIter.remove();
            }
        }

        Iterator<Octopus> octoIter = mOctopuses.iterator();
        while (octoIter.hasNext()) {
            if (octoIter.next().mPos.y < TreeBranch.sGlobal.y - 10f) {
                octoIter.remove();
            }
        }
    }

    private void spawnFishAbove() {
        boolean collided = false;
        Fish fish = new Fish(Constants.sRandom.nextFloat() * 30f - 15f + TreeBranch.sGlobal.x, TreeBranch.sGlobal.y + 10f);
        for (Octopus o : mOctopuses) {
            if (o.mBoundingBox.overlaps(fish.mBoundingBox)) {
                collided = true;
            }
        }
        for (Fish f : mFishes) {
            if (f.mBoundingBox.overlaps(fish.mBoundingBox)) {
                collided = true;
            }
        }
        if (!collided) {
            mFishes.add(fish);
        }
    }

    private void spawnOctoAbove() {
        boolean collided = false;
        Octopus octopus = new Octopus(Constants.sRandom.nextFloat() * 30f - 15f + TreeBranch.sGlobal.x, TreeBranch.sGlobal.y + 15f);
        for (Octopus o : mOctopuses) {
            if (o.mBoundingBox.overlaps(octopus.mBoundingBox)) {
                collided = true;
            }
        }
        for (Fish f : mFishes) {
            if (f.mBoundingBox.overlaps(octopus.mBoundingBox)) {
                collided = true;
            }
        }
        if (!collided) {
            mOctopuses.add(octopus);
        }
    }

    private void moveAndUpdateCamera() {
        TreeBranch.sGlobal.y += mSpeed;
        mTotalPoints += mSpeed;
        if ((TreeBranch.sGlobal.y > 10f && mCamera.zoom < 3f) ||
                (TreeBranch.sGlobal.y > 200f && mCamera.zoom < 5f)||
                (TreeBranch.sGlobal.y > 400f && mCamera.zoom < 5f)) {
            mCamera.zoom += 0.01f;
        }
        mCurrentViewCord.set(getAvgOfLast10X(),TreeBranch.sGlobal.y);
        mCamera.position.set(mCurrentViewCord, 0);
        mCamera.update();
    }

    private float getAvgOfLast10X() {
        mAvgX[mAvgXpos++] = TreeBranch.sGlobal.x;
        mAvgXpos %= 10;
        float avg = 0;
        for (int i=0; i< 10; i++) {
            avg += mAvgX[i];
        }
        avg /= 10f;
        return avg;
    }
}
